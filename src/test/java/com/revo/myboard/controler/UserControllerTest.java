package com.revo.myboard.controler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revo.myboard.user.Gender;
import com.revo.myboard.user.dto.DataDTO;
import com.revo.myboard.user.dto.EmailDTO;
import com.revo.myboard.user.dto.PasswordDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/*
 * Created By Revo
 */

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String SHOULD_GET_END_POINT = "/users/test";
    private static final String THROW_GET_END_POINT = "/users/error";

    private static final String SHOULD_SEARCH_END_POINT = "/users/test/search";
    private static final String SHOULD_SEARCH_LOGIN = "test";

    private static final String TOKEN = "ERROR_TOKEN";

    private static final String PASSWORD_END_POINT = "/users/password-reset";
    private static final String SHOULD_PASSWORD = "test2";
    private static final String THROW_PASSWORD = "t";

    private static final String EMAIL_END_POINT = "/users/email-reset";
    private static final String SHOULD_EMAIL = "newemail@email.pl";
    private static final String THROW_EMAIL = "newemailemail.pl";

    private static final String DATA_END_POINT = "/users/data-reset";
    private static final String DATA_CITY = "CITY";
    private static final String DATA_DESCRIPTION = "DESC";
    private static final String DATA_PAGE = "PAGE";
    private static final String DATA_EMPTY = "";

    private static final String SHOULD_PROFILE_END_POINT = "/users";

    private static final String SHOULD_DELETE_END_POINT_USER = "/users/userTokenDelete";
    private static final String SHOULD_DELETE_END_POINT_ADMIN = "/users/userDelete";
    private static final String THROW_DELETE_END_POINT_ADMIN = "/users/NOTEXISTSUSER";
    private static final String THROW_DELETE_END_POINT_USER = "/users/test";

    private static final String GET_SEXES_END_POINT = "/users/genders";

    private static final String ROLE_MODERATOR = "MODERATOR";
    private static final String ROLE_ADMIN = "ADMIN";

    private static final String SHOULD_BAN_END_POINT = "/users/notBannedForBan/ban";
    private static final String THROW_INVALID_BAN_END_POINT = "/users/bannedForBan/ban";
    private static final String THROW_NULL_BAN_END_POINT = "/users/ERROR/ban";

    private static final String SHOULD_UNBAN_END_POINT = "/users/bannedForUnban/unban";
    private static final String THROW_INVALID_UNBAN_END_POINT = "/users/notBannedForUnban/unban";
    private static final String THROW_NULL_UNBAN_END_POINT = "/users/ERROR/unban";

    private static final String SHOULD_SET_END_POINT = "/users/test/5";
    private static final String THROW_NULL_G_SET_END_POINT = "/users/test/-1";
    private static final String THROW_NULL_U_SET_END_POINT = "/users/test/-1";
    private static final Object TOKEN_USER_LOGIN = "userToken";
    private static final Object SHOULD_SET_GROUP = "userToken";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SHOULD_GET_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value(SHOULD_SEARCH_LOGIN));
    }

    @Test
    void shouldThrow404WhileGetting() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(THROW_GET_END_POINT)).andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldSearchUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SHOULD_SEARCH_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].login").value(SHOULD_SEARCH_LOGIN));
    }

    @Test
    void shouldChangePassword() throws Exception {
        var passwordDTO = new PasswordDTO();
        passwordDTO.setPassword(SHOULD_PASSWORD);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(passwordDTO);
        mockMvc.perform(MockMvcRequestBuilders.patch(PASSWORD_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForUserChangingPassword(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists());
    }

    @Test
    void shouldThrow400WhileChangingPassword() throws Exception {
        var passwordDTO = new PasswordDTO();
        passwordDTO.setPassword(THROW_PASSWORD);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(passwordDTO);
        mockMvc.perform(MockMvcRequestBuilders.patch(PASSWORD_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldChangeEmail() throws Exception {
        var emailDTO = new EmailDTO();
        emailDTO.setEmail(SHOULD_EMAIL);
        String json = objectMapper.writer().writeValueAsString(emailDTO);
        mockMvc.perform(MockMvcRequestBuilders.patch(EMAIL_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(SHOULD_EMAIL));
    }

    @Test
    void shouldThrow400WhileChangingEmail() throws Exception {
        var emailDTO = new EmailDTO();
        emailDTO.setEmail(THROW_EMAIL);
        String json = objectMapper.writer().writeValueAsString(emailDTO);
        mockMvc.perform(MockMvcRequestBuilders.patch(EMAIL_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldChangeData() throws Exception {
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(DataDTO.builder().age(18)
                .city(DATA_CITY).description(DATA_DESCRIPTION).page(DATA_PAGE).gender(Gender.FEMALE.toString()).build());
        mockMvc.perform(MockMvcRequestBuilders.patch(DATA_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.age").value(18))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.city").value(DATA_CITY))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value(DATA_DESCRIPTION))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page").value(DATA_PAGE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.gender").value(Gender.FEMALE.toString()));
    }

    @Test
    void shouldThrow400WhileChangingData() throws Exception {
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(DataDTO.builder().age(-1)
                .city(DATA_EMPTY).description(DATA_EMPTY).page(DATA_EMPTY).gender(Gender.FEMALE.toString()).build());
        mockMvc.perform(MockMvcRequestBuilders.patch(DATA_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldGetProfile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SHOULD_PROFILE_END_POINT).header(AUTHORIZATION_HEADER,
                Utils.getTokenForUser(mockMvc, objectMapper))).andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value(TOKEN_USER_LOGIN));
    }

    @Test
    void shouldThrowWhileGettingProfile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SHOULD_PROFILE_END_POINT).header(AUTHORIZATION_HEADER,
                TOKEN)).andExpect(MockMvcResultMatchers.status().is(401));
    }

    @Test
    void shouldDeleteUserAsUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(SHOULD_DELETE_END_POINT_USER).header(AUTHORIZATION_HEADER,
                        Utils.getTokenForUserDeleting(mockMvc, objectMapper)))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    void shouldThrow400WhileDeletingUserAsUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(THROW_DELETE_END_POINT_USER).header(AUTHORIZATION_HEADER,
                Utils.getTokenForUser(mockMvc, objectMapper))).andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldDeleteUserAsAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(SHOULD_DELETE_END_POINT_ADMIN).header(AUTHORIZATION_HEADER,
                Utils.getTokenForAdmin(mockMvc, objectMapper))).andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    void shouldThrow404WhileDeletingUserAsAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(THROW_DELETE_END_POINT_ADMIN).header(AUTHORIZATION_HEADER,
                Utils.getTokenForAdmin(mockMvc, objectMapper))).andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    @WithMockUser
    void shouldGetSexList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(GET_SEXES_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists());
    }

    @Test
    @WithMockUser(roles = ROLE_MODERATOR)
    void shouldBanUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(SHOULD_BAN_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.blocked").value(true));
    }

    @Test
    @WithMockUser(roles = ROLE_MODERATOR)
    void shouldThrow400WhileBanning() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(THROW_INVALID_BAN_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    @WithMockUser(roles = ROLE_MODERATOR)
    void shouldThrow404WhileBanning() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(THROW_NULL_BAN_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    @WithMockUser(roles = ROLE_MODERATOR)
    void shouldUnbanUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(SHOULD_UNBAN_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.blocked").value(false));
    }

    @Test
    @WithMockUser(roles = ROLE_MODERATOR)
    void shouldThrow400WhileUnbanning() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(THROW_INVALID_UNBAN_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    @WithMockUser(roles = ROLE_MODERATOR)
    void shouldThrow404WhileUnbanning() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(THROW_NULL_UNBAN_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    void shouldSetUserGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(SHOULD_SET_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value(SHOULD_SEARCH_LOGIN))
                .andExpect(MockMvcResultMatchers.jsonPath("$.group").value(SHOULD_SET_GROUP));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    void shouldThrows404WhileSettingGroupForGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(THROW_NULL_G_SET_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    void shouldThrows404WhileSettingGroupForUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(THROW_NULL_U_SET_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

}
