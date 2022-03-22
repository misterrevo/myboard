package com.revo.myboard.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revo.myboard.BaseIT;
import com.revo.myboard.Utils;
import com.revo.myboard.report.dto.ContentDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
public class ReportControllerTest extends BaseIT {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String SHOULD_GET_END_POINT = "/reports/4";
    private static final String FIRST_REPORT_END_POINT = "/reports/1";
    private static final String THROW_NULL_END_POINT = "/reports/-1";

    private static final String SHOULD_POST_END_POINT = "/reports/posts/1";
    private static final String SHOULD_POST_CONTENT = "testReportPost";
    private static final String THROW_POST_END_POINT = "/reports/posts/-1";
    private static final String EMPTY_STRING = "";

    private static final String SHOULD_COMMENT_END_POINT = "/reports/comments/1";
    private static final String THROW_COMMENT_END_POINT = "/reports/comments/-1";
    private static final String SHOULD_COMMENT_CONTENT = "testReportComment";

    private static final String ROLE_MODERATOR = "MODERATOR";
    private static final String ROLE_ADMIN = "ADMIN";

    private static final String THROW_INVALID_CHECKED_END_POINT = "/reports/2";

    private static final String GET_NOTCHECKED_END_POINT = "/reports/not-checked";

    private static final String SHOULD_DELETE_END_POINT = "/reports/3";
    private static final String ID_JSON_PATH = "$.id";
    private static final String CONTENT_JSON_PATH = "$.content";
    private static final String CHECKED_JSON_PATH = "$.checked";
    private static final String EMPTY_JSON_PATH = "$";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetReportAsUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SHOULD_GET_END_POINT).header(AUTHORIZATION_HEADER,
                        Utils.getTokenForUser(mockMvc, objectMapper))).andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath(ID_JSON_PATH).value(4));
    }

    @Test
    void shouldThrow404WhileGettingAsUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(FIRST_REPORT_END_POINT).header(AUTHORIZATION_HEADER,
                Utils.getTokenForUser(mockMvc, objectMapper))).andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldGetReportAsModerator() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SHOULD_GET_END_POINT).header(AUTHORIZATION_HEADER,
                        Utils.getTokenForModerator(mockMvc, objectMapper))).andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath(ID_JSON_PATH).value(4));
    }

    @Test
    void shouldThrow404WhileGettingAsModerator() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(THROW_NULL_END_POINT).header(AUTHORIZATION_HEADER,
                Utils.getTokenForModerator(mockMvc, objectMapper))).andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldReportPost() throws Exception {
        var contentDTO = new ContentDTO();
        contentDTO.setContent(SHOULD_POST_CONTENT);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(contentDTO);
        mockMvc.perform(MockMvcRequestBuilders.post(SHOULD_POST_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath(CONTENT_JSON_PATH).value(SHOULD_POST_CONTENT));
    }

    @Test
    void shouldThrow404WhileReportingPost() throws Exception {
        var contentDTO = new ContentDTO();
        contentDTO.setContent(SHOULD_POST_CONTENT);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(contentDTO);
        mockMvc.perform(MockMvcRequestBuilders.post(THROW_POST_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldThrow400WhileReportingPost() throws Exception {
        var contentDTO = new ContentDTO();
        contentDTO.setContent(EMPTY_STRING);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(contentDTO);
        mockMvc.perform(MockMvcRequestBuilders.post(SHOULD_POST_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldReportComment() throws Exception {
        var contentDTO = new ContentDTO();
        contentDTO.setContent(SHOULD_COMMENT_CONTENT);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(contentDTO);
        mockMvc.perform(MockMvcRequestBuilders.post(SHOULD_COMMENT_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath(CONTENT_JSON_PATH).value(SHOULD_COMMENT_CONTENT));
    }

    @Test
    void shouldThrow404WhileReportingComment() throws Exception {
        var contentDTO = new ContentDTO();
        contentDTO.setContent(SHOULD_COMMENT_CONTENT);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(contentDTO);
        mockMvc.perform(MockMvcRequestBuilders.post(THROW_COMMENT_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldThrow400WhileReportingComment() throws Exception {
        var contentDTO = new ContentDTO();
        contentDTO.setContent(EMPTY_STRING);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(contentDTO);
        mockMvc.perform(MockMvcRequestBuilders.post(SHOULD_COMMENT_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    @WithMockUser(roles = ROLE_MODERATOR)
    void shouldSetCheckedReport() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(FIRST_REPORT_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath(CHECKED_JSON_PATH).value(true));
    }

    @Test
    @WithMockUser(roles = ROLE_MODERATOR)
    void shouldThrow400WhileChecking() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(THROW_INVALID_CHECKED_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    @WithMockUser(roles = ROLE_MODERATOR)
    void shouldThrow404WhileChecking() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(THROW_NULL_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    @WithMockUser(roles = ROLE_MODERATOR)
    void shouldGetAllNotChecked() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(GET_NOTCHECKED_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath(EMPTY_JSON_PATH).exists());
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    void shouldDeleteReport() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(SHOULD_DELETE_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    void shouldThrow404WhileDeleting() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(THROW_NULL_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

}

