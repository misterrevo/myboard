package com.revo.myboard.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revo.myboard.Utils;
import com.revo.myboard.security.dto.CodeDTO;
import com.revo.myboard.security.dto.CredentialsDTO;
import com.revo.myboard.security.dto.RegisterDTO;
import com.revo.myboard.user.dto.EmailDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    private static final String LOGIN_END_POINT = "/login";
    private static final String SHOULD_LOGIN_VALUE = "test";
    private static final String THROW_UNAUTHORIZED_LOGIN_VALUE = "testError";

    private static final String REGISTER_END_POINT = "/register";
    private static final String SHOULD_REGISTER_EMAIL = "anetawolna55@wp.pl";
    private static final String SHOULD_REGISTER_VALUE = "register";
    private static final String THROW_REGISTER_EMAIL = "erroremail";

    private static final String RESEND_END_POINT = "/resend-code";
    private static final String SHOULD_RESEND_EMAIL = "gametspeak@wp.pl";
    private static final String THROW_NULL_RESEND_EMAIL = "notexists@email.pl";
    private static final String THROW_INVALID_RESEND_EMAIL = "notexistsemail.pl";

    private static final String ACTIVE_END_POINT = "/active";
    private static final String SHOULD_ACTIVE_CODE = "4849538f-db9e-4159-ad918-815878e433f";
    private static final String THROW_INVALID_ACTIVE_CODE = "4849538f";
    private static final String THROW_NULL_ACTIVE_CODE = "4849538f-XXXX-4159-ad9188-15878e433f";

    private static final String ACTIVE_END_POINT_ADMIN = "/active/adminActive";
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String THROW_NULL_END_POINT_ADMIN = "/active/notExistsUser";
    private static final String THROW_INVALID_END_POINT_ADMIN = "/active/test";
    private static final String LOGIN_JSON_PATH = "$.login";
    private static final String EMAIL_JSON_PATH = "$.email";
    private static final String ACTIVE_JSON_PATH = "$.active";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldLoginUser() throws Exception {
        var credentialsDTO = new CredentialsDTO();
        credentialsDTO.setLogin(SHOULD_LOGIN_VALUE);
        credentialsDTO.setPassword(SHOULD_LOGIN_VALUE);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(credentialsDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(LOGIN_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void shouldThrow401WhileLoging() throws Exception {
        var credentialsDTO = new CredentialsDTO();
        credentialsDTO.setLogin(THROW_UNAUTHORIZED_LOGIN_VALUE);
        credentialsDTO.setPassword(THROW_UNAUTHORIZED_LOGIN_VALUE);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(credentialsDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(LOGIN_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(401));
    }

    @Test
    void shouldRegisterUser() throws Exception {
        var registerDTO = new RegisterDTO();
        registerDTO.setEmail(SHOULD_REGISTER_EMAIL);
        registerDTO.setLogin(SHOULD_REGISTER_VALUE);
        registerDTO.setPassword(SHOULD_REGISTER_VALUE);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(registerDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(REGISTER_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath(LOGIN_JSON_PATH).value(SHOULD_REGISTER_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath(EMAIL_JSON_PATH).value(SHOULD_REGISTER_EMAIL));
    }

    @Test
    void shouldThrow400WhileRegistering() throws Exception {
        var registerDTO = new RegisterDTO();
        registerDTO.setEmail(THROW_REGISTER_EMAIL);
        registerDTO.setLogin(SHOULD_REGISTER_VALUE);
        registerDTO.setPassword(SHOULD_REGISTER_VALUE);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(registerDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(REGISTER_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldResendActivationCode() throws Exception {
        var emailDTO = new EmailDTO();
        emailDTO.setEmail(SHOULD_RESEND_EMAIL);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(emailDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(RESEND_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void shouldThrow404WhileCodeResending() throws Exception {
        var emailDTO = new EmailDTO();
        emailDTO.setEmail(THROW_NULL_RESEND_EMAIL);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(emailDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(RESEND_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldThrow400WhileCodeResending() throws Exception {
        var emailDTO = new EmailDTO();
        emailDTO.setEmail(THROW_INVALID_RESEND_EMAIL);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(emailDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(RESEND_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldActiveUser() throws Exception {
        var codeDTO = new CodeDTO();
        codeDTO.setCode(SHOULD_ACTIVE_CODE);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(codeDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(ACTIVE_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath(ACTIVE_JSON_PATH).value(true));
    }

    @Test
    void shouldThrow400WhileActivating() throws Exception {
        var codeDTO = new CodeDTO();
        codeDTO.setCode(THROW_INVALID_ACTIVE_CODE);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(codeDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(ACTIVE_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldThrow404WhileActivating() throws Exception {
        var codeDTO = new CodeDTO();
        codeDTO.setCode(THROW_NULL_ACTIVE_CODE);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(codeDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(ACTIVE_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    @WithMockUser(roles = ADMIN_ROLE)
    void shouldActiveUserAsAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(ACTIVE_END_POINT_ADMIN))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath(ACTIVE_JSON_PATH).value(true));
    }

    @Test
    @WithMockUser(roles = ADMIN_ROLE)
    void shouldThrow404WhileActivatingUserAsAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(THROW_NULL_END_POINT_ADMIN)).andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    @WithMockUser(roles = ADMIN_ROLE)
    void shouldThrow400WhileActivatingUserAsAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(THROW_INVALID_END_POINT_ADMIN)).andExpect(MockMvcResultMatchers.status().is(400));
    }

}
