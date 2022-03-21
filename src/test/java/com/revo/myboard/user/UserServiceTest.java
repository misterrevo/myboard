package com.revo.myboard.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.revo.myboard.email.EmailService;
import com.revo.myboard.group.Authority;
import com.revo.myboard.group.Group;
import com.revo.myboard.group.GroupServiceApi;
import com.revo.myboard.security.dto.RegisterDTO;
import com.revo.myboard.user.dto.DataDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String TEST_NAME = "TEST";
    private static final String TEST_NAME_NEW = "NEW NAME";
    private static final String SECRET_FIELD = "secret";
    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private GroupServiceApi groupService;
    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private RegisterDTO testRegisterDTO;
    private DataDTO testDataDTO;
    private Group testGroup;
    private String token;

    @BeforeEach
    void init(){
        ReflectionTestUtils.setField(userService, SECRET_FIELD, TEST_NAME);
        token = JWT.create()
                .withSubject(TEST_NAME)
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                .sign(Algorithm.HMAC256(String.valueOf(ReflectionTestUtils.getField(userService, SECRET_FIELD))));
        testUser = User.builder()
                .login(TEST_NAME)
                .group(Group.builder()
                        .name(TEST_NAME)
                        .authority(Authority.USER)
                        .build())
                .email(TEST_NAME)
                .data(Data.builder()
                        .description(TEST_NAME)
                        .age(18)
                        .city(TEST_NAME)
                        .page(TEST_NAME)
                        .gender(Gender.MALE)
                        .build())
                .blocked(false)
                .active(false)
                .lastActivityDate(LocalDateTime.now())
                .build();
        testGroup = Group.builder()
                .id(1L)
                .name(TEST_NAME_NEW)
                .authority(Authority.ADMIN)
                .build();
        testRegisterDTO = new RegisterDTO();
        testRegisterDTO.setEmail(TEST_NAME);
        testRegisterDTO.setLogin(TEST_NAME);
        testRegisterDTO.setPassword(TEST_NAME);
        testDataDTO = new DataDTO();
        testDataDTO.setAge(1);
        testDataDTO.setCity(TEST_NAME_NEW);
        testDataDTO.setDescription(TEST_NAME_NEW);
        testDataDTO.setPage(TEST_NAME_NEW);
        testDataDTO.setGender(Gender.FEMALE.toString());
    }

    @Test
    void registerUser() {
        //given
        Mockito.when(userRepository.existsByEmail(Mockito.any(String.class))).thenReturn(false);
        Mockito.when(userRepository.existsByLogin(Mockito.any(String.class))).thenReturn(false);
        Mockito.when(userRepository.existsByCode(Mockito.any(String.class))).thenReturn(false);
        Mockito.doNothing().when(emailService).sendActiavtionLink(Mockito.anyString(), Mockito.anyString());
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(testUser);
        //when
        var userDTO = userService.registerUser(testRegisterDTO);
        //then
        Assertions.assertNotNull(userDTO.getData());
        Assertions.assertEquals(userDTO.getGroup(), TEST_NAME);
        Assertions.assertEquals(userDTO.getEmail(), TEST_NAME);
        Assertions.assertEquals(userDTO.getPosts().size(), 0);
        Assertions.assertEquals(userDTO.getLogin(), TEST_NAME);
        Assertions.assertFalse(userDTO.isBlocked());
        Assertions.assertFalse(userDTO.isActive());
    }

    @Test
    void resendActivationCode() {
        //given
        Mockito.doNothing().when(emailService).sendActiavtionLink(Mockito.anyString(), Mockito.anyString());
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(testUser));
        //when
        var userDTO = userService.resendActivationCode(TEST_NAME);
        //then
        Assertions.assertNotNull(userDTO.getData());
        Assertions.assertEquals(userDTO.getGroup(), TEST_NAME);
        Assertions.assertEquals(userDTO.getEmail(), TEST_NAME);
        Assertions.assertEquals(userDTO.getPosts().size(), 0);
        Assertions.assertEquals(userDTO.getLogin(), TEST_NAME);
        Assertions.assertFalse(userDTO.isBlocked());
        Assertions.assertFalse(userDTO.isActive());
    }

    @Test
    void searchUsersByLogin() {
        //given
        Mockito.when(userRepository.findByLoginContaining(Mockito.anyString())).thenReturn(Arrays.asList(testUser));
        //when
        var searchesDTO = userService.searchUsersByLogin(TEST_NAME);
        //then
        Assertions.assertEquals(searchesDTO.size(), 1);
        Assertions.assertEquals(searchesDTO.get(0).getLogin(), TEST_NAME);
        Assertions.assertNotNull(searchesDTO.get(0).getLastActivity());
    }

    @Test
    void activeUserByCode() {
        //given
        Mockito.when(userRepository.findByCode(Mockito.anyString())).thenReturn(Optional.of(testUser));
        //when
        var userDTO = userService.activeUserByCode(TEST_NAME);
        //then
        Assertions.assertNotNull(userDTO.getData());
        Assertions.assertEquals(userDTO.getGroup(), TEST_NAME);
        Assertions.assertEquals(userDTO.getEmail(), TEST_NAME);
        Assertions.assertEquals(userDTO.getPosts().size(), 0);
        Assertions.assertEquals(userDTO.getLogin(), TEST_NAME);
        Assertions.assertFalse(userDTO.isBlocked());
        Assertions.assertTrue(userDTO.isActive());
    }

    @Test
    void currentLoggedUserProfileDTO() {
        //given
        Mockito.when(userRepository.findByLogin(Mockito.anyString())).thenReturn(Optional.of(testUser));
        //when
        var profileDTO = userService.currentLoggedUserProfileDTO(token);
        //then
        Assertions.assertNotNull(profileDTO.getData());
        Assertions.assertEquals(profileDTO.getGroup(), TEST_NAME);
        Assertions.assertEquals(profileDTO.getEmail(), TEST_NAME);
        Assertions.assertEquals(profileDTO.getPosts().size(), 0);
        Assertions.assertEquals(profileDTO.getLogin(), TEST_NAME);
        Assertions.assertEquals(profileDTO.getAuthority(), Authority.USER.toString());
        Assertions.assertEquals(profileDTO.getPosts().size(), 0);
        Assertions.assertEquals(profileDTO.getComments().size(), 0);
        Assertions.assertEquals(profileDTO.getReports().size(), 0);
        Assertions.assertEquals(profileDTO.getLiked().size(), 0);
    }

    @Test
    void getUserDTOByLogin() {
        //given
        Mockito.when(userRepository.findByLogin(Mockito.anyString())).thenReturn(Optional.of(testUser));
        //when
        var userDTO = userService.getUserDTOByLogin(TEST_NAME);
        //then
        Assertions.assertNotNull(userDTO.getData());
        Assertions.assertEquals(userDTO.getGroup(), TEST_NAME);
        Assertions.assertEquals(userDTO.getEmail(), TEST_NAME);
        Assertions.assertEquals(userDTO.getPosts().size(), 0);
        Assertions.assertEquals(userDTO.getLogin(), TEST_NAME);
        Assertions.assertFalse(userDTO.isBlocked());
        Assertions.assertFalse(userDTO.isActive());
    }

    @Test
    void changeUserPassword() {
        //given
        Mockito.when(userRepository.findByLogin(Mockito.anyString())).thenReturn(Optional.of(testUser));
        //when
        var userDTO = userService.changeUserPassword(token, TEST_NAME_NEW);
        //then
        Assertions.assertNotNull(userDTO.getData());
        Assertions.assertEquals(userDTO.getGroup(), TEST_NAME);
        Assertions.assertEquals(userDTO.getEmail(), TEST_NAME);
        Assertions.assertEquals(userDTO.getPosts().size(), 0);
        Assertions.assertEquals(userDTO.getLogin(), TEST_NAME);
        Assertions.assertFalse(userDTO.isBlocked());
        Assertions.assertFalse(userDTO.isActive());
    }

    @Test
    void banUserByLogin() {
        //given
        Mockito.when(userRepository.findByLogin(Mockito.anyString())).thenReturn(Optional.of(testUser));
        //when
        var userDTO = userService.banUserByLogin(TEST_NAME);
        //then
        Assertions.assertNotNull(userDTO.getData());
        Assertions.assertEquals(userDTO.getGroup(), TEST_NAME);
        Assertions.assertEquals(userDTO.getEmail(), TEST_NAME);
        Assertions.assertEquals(userDTO.getPosts().size(), 0);
        Assertions.assertEquals(userDTO.getLogin(), TEST_NAME);
        Assertions.assertTrue(userDTO.isBlocked());
        Assertions.assertFalse(userDTO.isActive());
    }

    @Test
    void unbanUserByLogin() {
        //given
        testUser.setBlocked(true);
        Mockito.when(userRepository.findByLogin(Mockito.anyString())).thenReturn(Optional.of(testUser));
        //when
        var userDTO = userService.unbanUserByLogin(TEST_NAME);
        //then
        Assertions.assertNotNull(userDTO.getData());
        Assertions.assertEquals(userDTO.getGroup(), TEST_NAME);
        Assertions.assertEquals(userDTO.getEmail(), TEST_NAME);
        Assertions.assertEquals(userDTO.getPosts().size(), 0);
        Assertions.assertEquals(userDTO.getLogin(), TEST_NAME);
        Assertions.assertFalse(userDTO.isBlocked());
        Assertions.assertFalse(userDTO.isActive());
    }

    @Test
    void changeUserData() {
        //given
        Mockito.when(userRepository.findByLogin(Mockito.anyString())).thenReturn(Optional.of(testUser));
        //when
        var userDTO = userService.changeUserData(token, testDataDTO);
        //then
        Assertions.assertEquals(userDTO.getData().getAge(), 1);
        Assertions.assertEquals(userDTO.getData().getCity(), TEST_NAME_NEW);
        Assertions.assertEquals(userDTO.getData().getPage(), TEST_NAME_NEW);
        Assertions.assertEquals(userDTO.getData().getDescription(), TEST_NAME_NEW);
        Assertions.assertEquals(userDTO.getData().getGender(), Gender.FEMALE.toString());
        Assertions.assertEquals(userDTO.getGroup(), TEST_NAME);
        Assertions.assertEquals(userDTO.getEmail(), TEST_NAME);
        Assertions.assertEquals(userDTO.getPosts().size(), 0);
        Assertions.assertEquals(userDTO.getLogin(), TEST_NAME);
        Assertions.assertFalse(userDTO.isBlocked());
        Assertions.assertFalse(userDTO.isActive());
    }

    @Test
    void changeUserEmail() {
        //given
        Mockito.when(userRepository.findByLogin(Mockito.anyString())).thenReturn(Optional.of(testUser));
        //when
        var userDTO = userService.changeUserEmail(token, TEST_NAME_NEW);
        //then
        Assertions.assertNotNull(userDTO.getData());
        Assertions.assertEquals(userDTO.getGroup(), TEST_NAME);
        Assertions.assertEquals(userDTO.getEmail(), TEST_NAME_NEW);
        Assertions.assertEquals(userDTO.getPosts().size(), 0);
        Assertions.assertEquals(userDTO.getLogin(), TEST_NAME);
        Assertions.assertFalse(userDTO.isBlocked());
        Assertions.assertFalse(userDTO.isActive());
    }

    @Test
    void setGroupByLogin() {
        //given
        Mockito.when(userRepository.findByLogin(Mockito.anyString())).thenReturn(Optional.of(testUser));
        Mockito.when(groupService.getGroupById(1L)).thenReturn(testGroup);
        //when
        var userDTO = userService.setGroupByLogin(TEST_NAME, 1L);
        //then
        Assertions.assertNotNull(userDTO.getData());
        Assertions.assertEquals(userDTO.getGroup(), TEST_NAME_NEW);
        Assertions.assertEquals(userDTO.getEmail(), TEST_NAME);
        Assertions.assertEquals(userDTO.getPosts().size(), 0);
        Assertions.assertEquals(userDTO.getLogin(), TEST_NAME);
        Assertions.assertFalse(userDTO.isBlocked());
        Assertions.assertFalse(userDTO.isActive());
    }

    @Test
    void activeByLogin() {
        //given
        Mockito.when(userRepository.findByLogin(Mockito.anyString())).thenReturn(Optional.of(testUser));
        //when
        var userDTO = userService.activeByLogin(TEST_NAME);
        //then
        Assertions.assertNotNull(userDTO.getData());
        Assertions.assertEquals(userDTO.getGroup(), TEST_NAME);
        Assertions.assertEquals(userDTO.getEmail(), TEST_NAME);
        Assertions.assertEquals(userDTO.getPosts().size(), 0);
        Assertions.assertEquals(userDTO.getLogin(), TEST_NAME);
        Assertions.assertFalse(userDTO.isBlocked());
        Assertions.assertTrue(userDTO.isActive());
    }
}