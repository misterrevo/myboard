package com.revo.myboard.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.revo.myboard.email.EmailService;
import com.revo.myboard.exception.EmailInUseException;
import com.revo.myboard.exception.LoginInUseException;
import com.revo.myboard.exception.MatchPasswordException;
import com.revo.myboard.exception.NoPermissionException;
import com.revo.myboard.exception.SameAccessStatusException;
import com.revo.myboard.exception.UserIsActiveException;
import com.revo.myboard.exception.UserNotExistsException;
import com.revo.myboard.group.Authority;
import com.revo.myboard.group.Group;
import com.revo.myboard.group.GroupServiceApi;
import com.revo.myboard.security.dto.RegisterDTO;
import com.revo.myboard.user.dto.DataDTO;
import com.revo.myboard.user.dto.ProfileDTO;
import com.revo.myboard.user.dto.SearchDTO;
import com.revo.myboard.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
class UserService implements UserServiceApi {

    private static final String CITY = "Brak";
    private static final String DESCRIPTION = "Brak";
    private static final String PAGE = "Brak";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String TOKEN_REPLACEMENT = "";

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final EmailService emailService;
    private final GroupServiceApi groupService;
    @Value("${spring.security.jwt.secret}")
    private String secret;

    @Override
    public List<User> getAll(){
        return repository.findAll();
    }

    @Override
    public UserDTO registerUser(RegisterDTO registerDTO) {
        var email = registerDTO.getEmail();
        if (existsByEmail(email)) {
            throw new EmailInUseException(email);
        }
        var login = registerDTO.getLogin();
        if (existsByLogin(login)) {
            throw new LoginInUseException(login);
        }
        var password = registerDTO.getPassword();
        var code = getCode();
        while (codeIsInUse(code)) {
            code = getCode();
        }
        emailService.sendActiavtionLink(email, code);
        var user = repository.save(buildUser(login, password, email, code));
        return mapFromUser(user);
    }

    private boolean codeIsInUse(String code) {
        return repository.existsByCode(code);
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

    @Override
    public UserDTO resendActivationCode(String email) {
        var user = getUserByEmail(email);
        if (user.isActive()) {
            throw new UserIsActiveException(email);
        }
        var code = getCode();
        while (codeIsInUse(code)) {
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
        return users.stream()
                .map(this::mapFromUserAsSearch)
                .toList();
    }

    private SearchDTO mapFromUserAsSearch(User user){
        return UserMapper.mapSearchDTOFromUser(user);
    }

    private List<User> findByLoginContaining(String login){
        return repository.findByLoginContaining(login);
    }

    @Override
    public UserDTO activeUserByCode(String code) {
        var user = findByCode(code);
        if (user.isActive()) {
            throw new UserIsActiveException(user.getEmail());
        }
        user.setActive(true);
        return mapFromUser(user);
    }

    private User findByCode(String code){
        return repository.findByCode(code)
                .orElseThrow(() -> new UserNotExistsException(code));
    }

    @Override
    public User currentLoggedUser(String token) {
        return getUserByLogin(getName(token));
    }

    private String getName(String token){
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(TOKEN_PREFIX, TOKEN_REPLACEMENT))
                .getSubject();
    }

    ProfileDTO currentLoggedUserProfileDTO(String token) {
        var user = currentLoggedUser(token);
        return mapFromUserAsProfile(user);
    }

    private ProfileDTO mapFromUserAsProfile(User user){
        return UserMapper.mapProfileDTOFromUser(user);
    }

    @Override
    public User getUserByLogin(String login) {
        return repository.findByLogin(login)
                .orElseThrow(() -> new UserNotExistsException(login));
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

    UserDTO changeUserData(String token, DataDTO dataDTO) {
        var user = currentLoggedUser(token);
        var data = user.getData();
        var gender = dataDTO.getGender();
        data.setDescription(dataDTO.getDescription());
        data.setAge(dataDTO.getAge());
        data.setCity(dataDTO.getCity());
        data.setPage(dataDTO.getPage());
        data.setGender(Gender.valueOf(gender.toUpperCase()));
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
        if (!hasAdminRole(user) && !isSameUser(user, userTarget)) {
            throw new NoPermissionException(login);
        }
        repository.delete(userTarget);
    }

    private boolean isSameUser(User user, User userTarget) {
        return Objects.equals(user, userTarget);
    }

    private boolean hasAdminRole(User user) {
        var group = user.getGroup();
        return Objects.equals(group.getAuthority(), Authority.ADMIN);
    }

    List<String> getGenderList() {
        return mapFromList();
    }

    private List<String> mapFromList(){
        return Arrays.stream(Gender.values())
                .map(Enum::toString)
                .toList();
    }

    @Override
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
