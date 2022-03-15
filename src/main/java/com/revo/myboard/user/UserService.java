package com.revo.myboard.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.revo.myboard.email.EmailService;
import com.revo.myboard.exception.*;
import com.revo.myboard.group.Authority;
import com.revo.myboard.group.Group;
import com.revo.myboard.group.GroupService;
import com.revo.myboard.user.dto.ProfileDTO;
import com.revo.myboard.user.dto.SearchDTO;
import com.revo.myboard.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*
 * Created By Revo
 */

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private static final String CITY = "Brak";
    private static final String DESCRIPTION = "Brak";
    private static final String PAGE = "Brak";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String TOKEN_REPLACEMENT = "";

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final EmailService emailService;
    private final GroupService groupService;
    @Value("${spring.security.jwt.secret}")
    private String secret;

    public UserDTO registerUser(String login, String password, String email) {
        if (existsByEmail(email)) {
            throw new EmailInUseException(email);
        }
        if (existsByLogin(login)) {
            throw new LoginInUseException(login);
        }
        var code = getCode();
        while (repository.findByCode(code).isPresent()) {
            code = getCode();
        }
        emailService.sendActiavtionLink(email, code);
        var user = repository.save(buildUser(login, password, email, code));
        return mapFromUser(user);
    }

    private String getCode(){
        return UUID.randomUUID().toString();
    }

    private boolean existsByEmail(String email){
        return repository.existsByEmail(email);
    }

    private boolean existsByLogin(String login){
        return repository.existsByLogin(login);
    }

    private User buildUser(String login, String password, String email, String code){
        return User.builder()
                .login(login)
                .password(encoder.encode(password))
                .email(email).code(code)
                .data(Data.builder()
                        .age(18)
                        .city(CITY)
                        .description(DESCRIPTION)
                        .page(PAGE)
                        .gender(Gender.MALE)
                        .build())
                .group(groupService.getDefaultGroup())
                .build();
    }

    public UserDTO resendActivationCode(String email) {
        var user = getUserByEmail(email);
        if (user.isActive()) {
            throw new UserIsActiveException(email);
        }
        var code = getCode();
        while (repository.findByCode(code).isPresent()) {
            code = getCode();
        }
        user.setCode(code);
        emailService.sendActiavtionLink(email, code);
        return mapFromUser(user);
    }

    private User getUserByEmail(String email){
        return repository.findByEmail(email).orElseThrow(() -> new UserNotExistsException(email));
    }

    List<SearchDTO> searchUsersByLogin(String login) {
        var result = findByLoginContaining(login);
        return mapFromList(result);
    }

    private List<SearchDTO> mapFromList(List<User> users){
        return users.stream().map(this::mapFromUserAsSearch).collect(Collectors.toList());
    }

    private SearchDTO mapFromUserAsSearch(User user){
        return UserMapper.mapSearchDTOFromUser(user);
    }

    private List<User> findByLoginContaining(String login){
        return repository.findByLoginContaining(login);
    }

    public UserDTO activeUserByCode(String code) {
        var user = findByCode(code);
        if (user.isActive()) {
            throw new UserIsActiveException(user.getEmail());
        }
        user.setActive(true);
        return mapFromUser(user);
    }

    private User findByCode(String code){
        return repository.findByCode(code).orElseThrow(() -> new UserNotExistsException(code));
    }

    public User currentLoggedUser(String token) {
        return getUserByLogin(getName(token));
    }

    private String getName(String token){
        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token.replace(TOKEN_PREFIX, TOKEN_REPLACEMENT)).getSubject();
    }

    public ProfileDTO currentLoggedUserProfileDTO(String token) {
        var user = currentLoggedUser(token);
        return mapFromUserAsProfile(user);
    }

    private ProfileDTO mapFromUserAsProfile(User user){
        return UserMapper.mapProfileDTOFromUser(user);
    }

    public User getUserByLogin(String login) {
        return repository.findByLogin(login).orElseThrow(() -> new UserNotExistsException(login));
    }

    UserDTO getUserDTOByLogin(String login){
        return mapFromUser(getUserByLogin(login));
    }

    UserDTO changeUserPassword(String token, String new_password) {
        var user = currentLoggedUser(token);
        if (encoder.matches(new_password, user.getPassword())) {
            throw new MatchPasswordException(new_password);
        }
        user.setPassword(encoder.encode(new_password));
        return mapFromUser(user);
    }

    UserDTO banUserByLogin(String login) {
        var user = getUserByLogin(login);
        if (user.isBlocked()) {
            throw new SameAccessStatusException(login, true);
        }
        user.setBlocked(true);
        return mapFromUser(user);
    }

    UserDTO unbanUserByLogin(String login) {
        var user = getUserByLogin(login);
        if (!user.isBlocked()) {
            throw new SameAccessStatusException(login, false);
        }
        user.setBlocked(false);
        return mapFromUser(user);
    }

    UserDTO changeUserData(String token, String description, int age, String city, String page, String sex) {
        var user = currentLoggedUser(token);
        var data = user.getData();
        data.setDescription(description);
        data.setAge(age);
        data.setCity(city);
        data.setPage(page);
        data.setGender(Gender.valueOf(sex.toUpperCase()));
        return mapFromUser(user);
    }

    UserDTO changeUserEmail(String token, String new_email) {
        var user = currentLoggedUser(token);
        user.setEmail(new_email);
        return mapFromUser(user);
    }

    UserDTO setGroupByLogin(String login, long group_id) {
        var user = getUserByLogin(login);
        user.setGroup(getGroup(group_id));
        return mapFromUser(user);
    }

    private Group getGroup(long id){
        return groupService.getGroupById(id);
    }

    void deleteUserByLogin(String token, String login) {
        var user = currentLoggedUser(token);
        var userTarget = getUserByLogin(login);
        if (!user.getGroup().getAuthority().equals(Authority.ADMIN) && !user.equals(userTarget)) {
            throw new NoPermissionException(login);
        }
        repository.delete(userTarget);
    }

    List<String> getSexList() {
        return mapFromList();
    }

    private List<String> mapFromList(){
        return Arrays.stream(Gender.values()).map(Enum::toString).collect(Collectors.toList());
    }

    public UserDTO activeByLogin(String login) {
        var user = getUserByLogin(login);
        if (user.isActive()) {
            throw new SameAccessStatusException(login, true);
        }
        user.setActive(true);
        return mapFromUser(user);
    }

    private UserDTO mapFromUser(User user){
        return UserMapper.mapUserDTOFromUser(user);
    }

}
