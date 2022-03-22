package com.revo.myboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revo.myboard.security.dto.CredentialsDTO;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

public class Utils {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private static final String LOGIN_END_POINT = "/login";
    private static final String USER_LOGIN = "userToken";
    private static final String MODERATOR_LOGIN = "moderatorToken";
    private static final String ADMIN_LOGIN = "adminToken";
    private static final String USER_LOGIN_PASWORD = "userTokenPassword";
    private static final String USER_LOGIN_DELETE = "userTokenDelete";
    private static final String USER_PASSWORD = "test";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    public static String getTokenForUser(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        var credentialsDTO = new CredentialsDTO();
        credentialsDTO.setLogin(USER_LOGIN);
        credentialsDTO.setPassword(USER_PASSWORD);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(credentialsDTO);
        return mockMvc.perform(
                        MockMvcRequestBuilders.post(LOGIN_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn().getResponse().getHeader(AUTHORIZATION_HEADER);
    }

    public static String getTokenForUserChangingPassword(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        var credentialsDTO = new CredentialsDTO();
        credentialsDTO.setLogin(USER_LOGIN_PASWORD);
        credentialsDTO.setPassword(USER_PASSWORD);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(credentialsDTO);
        return mockMvc.perform(
                        MockMvcRequestBuilders.post(LOGIN_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn().getResponse().getHeader(AUTHORIZATION_HEADER);
    }

    public static String getTokenForUserDeleting(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        var credentialsDTO = new CredentialsDTO();
        credentialsDTO.setLogin(USER_LOGIN_DELETE);
        credentialsDTO.setPassword(USER_PASSWORD);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(credentialsDTO);
        return mockMvc.perform(
                        MockMvcRequestBuilders.post(LOGIN_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn().getResponse().getHeader(AUTHORIZATION_HEADER);
    }

    public static String getTokenForModerator(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        var credentialsDTO = new CredentialsDTO();
        credentialsDTO.setLogin(MODERATOR_LOGIN);
        credentialsDTO.setPassword(USER_PASSWORD);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(credentialsDTO);
        return mockMvc.perform(
                        MockMvcRequestBuilders.post(LOGIN_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn().getResponse().getHeader(AUTHORIZATION_HEADER);
    }

    public static String getTokenForAdmin(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        var credentialsDTO = new CredentialsDTO();
        credentialsDTO.setLogin(ADMIN_LOGIN);
        credentialsDTO.setPassword(USER_PASSWORD);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(credentialsDTO);
        return mockMvc.perform(
                        MockMvcRequestBuilders.post(LOGIN_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn().getResponse().getHeader(AUTHORIZATION_HEADER);
    }

}
