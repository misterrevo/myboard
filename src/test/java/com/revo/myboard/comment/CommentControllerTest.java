package com.revo.myboard.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revo.myboard.Utils;
import com.revo.myboard.comment.dto.ContentDTO;
import com.revo.myboard.comment.dto.CreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/*
 * Created By Revo
 */

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String FIRST_COMMENT_END_POINT = "/comments/1";
    private static final String THROW_NULL_END_POINT = "/comments/-1";

    private static final String CREATE_END_POINT = "/comments";
    private static final String SHOULD_CREATE_CONTENT = "testComment";

    private static final String SHOULD_EDIT_END_POINT = "/comments/2";
    private static final String SHOULD_EDIT_CONTENT_USER = "testRenameUserChanged";
    private static final String THROW_EDIT_CONTENT_USER = "";
    private static final String SHOULD_EDIT_CONTENT_MODERATOR = "testRenameModeratorChanged";

    private static final String SHOULD_DELETE_END_POINT_USER = "/comments/3";
    private static final String SHOULD_DELETE_END_POINT_MODERATOR = "/comments/4";

    private static final String SHOULD_LIKE_END_POINT = "/comments/6/like";
    private static final String THROW_LIKE_END_POINT = "/comments/5/like";

    private static final String SHOULD_UNLIKE_END_POINT = "/comments/7/unlike";
    private static final String THROW_UNLIKE_END_POINT = "/comments/8/unlike";
    private static final String ID_JSON_PATH = "$.id";
    private static final String CONTENT_JSON_PATH = "$.content";
    private static final String WHO_JSON_PATH = "$.who";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(FIRST_COMMENT_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath(ID_JSON_PATH).value(1));
    }

    @Test
    void shouldThrow404WhileGetting() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(THROW_NULL_END_POINT)).andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldCreateComment() throws Exception {
        var createDTO = new CreateDTO();
        createDTO.setContent(SHOULD_CREATE_CONTENT);
        createDTO.setPost(1);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(CREATE_END_POINT).header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper)).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath(CONTENT_JSON_PATH).value(SHOULD_CREATE_CONTENT));
    }

    @Test
    void shouldThrow404WhileCreating() throws Exception {
        var createDTO = new CreateDTO();
        createDTO.setContent(SHOULD_CREATE_CONTENT);
        createDTO.setPost(-1);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(CREATE_END_POINT).header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper)).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldEditCommentAsUser() throws Exception {
        var contentDTO = new ContentDTO();
        contentDTO.setNewContent(SHOULD_EDIT_CONTENT_USER);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(contentDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(SHOULD_EDIT_END_POINT).header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper)).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath(CONTENT_JSON_PATH).value(SHOULD_EDIT_CONTENT_USER));
    }

    @Test
    void shouldThrowNoPermWhileEditingAsUser() throws Exception {
        var contentDTO = new ContentDTO();
        contentDTO.setNewContent(SHOULD_EDIT_CONTENT_USER);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(contentDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(FIRST_COMMENT_END_POINT).header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper)).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldThrow400WhileEditingAsUser() throws Exception {
        var contentDTO = new ContentDTO();
        contentDTO.setNewContent(THROW_EDIT_CONTENT_USER);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(contentDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(FIRST_COMMENT_END_POINT).header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper)).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldEditCommentAsModerator() throws Exception {
        var contentDTO = new ContentDTO();
        contentDTO.setNewContent(SHOULD_EDIT_CONTENT_MODERATOR);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(contentDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(SHOULD_EDIT_END_POINT).header(AUTHORIZATION_HEADER, Utils.getTokenForModerator(mockMvc, objectMapper)).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath(CONTENT_JSON_PATH).value(SHOULD_EDIT_CONTENT_MODERATOR));
    }

    @Test
    void shouldThrow404WhileEditingAsModerator() throws Exception {
        var contentDTO = new ContentDTO();
        contentDTO.setNewContent(SHOULD_EDIT_CONTENT_MODERATOR);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(contentDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(THROW_NULL_END_POINT).header(AUTHORIZATION_HEADER, Utils.getTokenForModerator(mockMvc, objectMapper)).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldDeleteCommentAsUser() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete(SHOULD_DELETE_END_POINT_USER).header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper)))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    void shouldThrow400WhileDeletingAsUser() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete(FIRST_COMMENT_END_POINT).header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper)))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldDeleteCommentAsModerator() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete(SHOULD_DELETE_END_POINT_MODERATOR).header(AUTHORIZATION_HEADER, Utils.getTokenForModerator(mockMvc, objectMapper)))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    void shouldThrow404WhileDeletingAsModerator() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete(THROW_NULL_END_POINT).header(AUTHORIZATION_HEADER, Utils.getTokenForModerator(mockMvc, objectMapper)))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldGiveLike() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(SHOULD_LIKE_END_POINT).header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper)))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath(WHO_JSON_PATH).exists());
    }

    @Test
    void shouldThrow400WhileGivingLike() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(THROW_LIKE_END_POINT).header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper)))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldRemoveLike() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(SHOULD_UNLIKE_END_POINT).header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper)))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void shouldThrow404WhileRemovingLike() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(THROW_UNLIKE_END_POINT).header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper)))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

}

