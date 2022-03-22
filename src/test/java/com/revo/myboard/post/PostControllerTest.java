package com.revo.myboard.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revo.myboard.BaseIT;
import com.revo.myboard.Utils;
import com.revo.myboard.post.dto.CreateDTO;
import com.revo.myboard.post.dto.EditDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
public class PostControllerTest extends BaseIT {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String SHOULD_GET_END_POINT = "/posts/1";
    private static final String THROW_NULL_END_POINT = "/posts/-1";

    private static final String SHOULD_GET_LAST_ACTIVE_END_POINT = "/posts/last-active";

    private static final String SHOULD_GET_MOST_LIKED_END_POINT = "/posts/most-liked";

    private static final String SHOULD_SEARCH_END_POINT = "/posts?title=testGet";
    private static final Object SHOULD_SEARCH_TITLE = "testGet";

    private static final String CREATE_END_POINT = "/posts";
    private static final String SHOULD_CREATE_VALUE = "testCreate";
    private static final String THROW_INVALID_CREATE_TITLE = "testGet";
    private static final String THROW_NULL_CREATE_TITLE = "testGet404";

    private static final String SHOULD_DELETE_END_POINT_USER = "/posts/2";
    private static final String SHOULD_DELETE_END_POINT_MODERATOR = "/posts/3";
    private static final String THROW_DELETE_END_POINT_USER = "/posts/1";

    private static final String SHOULD_EDIT_END_POINT = "/posts/4";
    private static final String THROW_EDIT_END_POINT_USER = "/posts/1";
    private static final String SHOULD_EDIT_TITLE_USER = "testRenameUserChanged";
    private static final String SHOULD_EDIT_TITLE_MODERATOR = "testRenameModeratorChanged";
    private static final String THROW_EDIT_TITLE = "t";
    private static final String SHOULD_EDIT_CONTENT = "testRenameChanged";

    private static final String SHOULD_LIKE_END_POINT = "/posts/6/like";
    private static final String THROW_LIKE_END_POINT = "/posts/5/like";

    private static final String SHOULD_UNLIKE_END_POINT = "/posts/7/unlike";
    private static final String THROW_UNLIKE_END_POINT = "/posts/8/unlike";
    private static final String ID_JSON_PATH = "$.id";
    private static final String ARRAY_TITLE_JSON_PATH = "$.[0].title";
    private static final String TITLE_JSON_PATH = "$.title";
    private static final String CONTENT_JSON_PATH = "$.content";
    private static final String WHO_JSON_PATH = "$.who";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SHOULD_GET_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath(ID_JSON_PATH).value(1));
    }

    @Test
    void shouldThrow404WhileGetting() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(THROW_NULL_END_POINT)).andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldGetLastActivePosts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SHOULD_GET_LAST_ACTIVE_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void shouldGetMostLikedPosts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SHOULD_GET_MOST_LIKED_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void shouldSearchPosts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SHOULD_SEARCH_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath(ARRAY_TITLE_JSON_PATH).value(SHOULD_SEARCH_TITLE));
    }

    @Test
    void shouldCreatePost() throws Exception {
        var createDTO = new CreateDTO();
        createDTO.setCategory(1);
        createDTO.setContent(SHOULD_CREATE_VALUE);
        createDTO.setTitle(SHOULD_CREATE_VALUE);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createDTO);
        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath(TITLE_JSON_PATH).value(SHOULD_CREATE_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath(CONTENT_JSON_PATH).value(SHOULD_CREATE_VALUE));
    }

    @Test
    void shouldThrow400WhileCreating() throws Exception {
        var createDTO = new CreateDTO();
        createDTO.setCategory(1);
        createDTO.setContent(SHOULD_CREATE_VALUE);
        createDTO.setTitle(THROW_INVALID_CREATE_TITLE);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createDTO);
        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldThrow404WhileCreating() throws Exception {
        var createDTO = new CreateDTO();
        createDTO.setCategory(-1);
        createDTO.setContent(SHOULD_CREATE_VALUE);
        createDTO.setTitle(THROW_NULL_CREATE_TITLE);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createDTO);
        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldDeletePostAsUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(SHOULD_DELETE_END_POINT_USER).header(AUTHORIZATION_HEADER,
                Utils.getTokenForUser(mockMvc, objectMapper))).andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    void shouldThrow404WhileDeletingAsUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(THROW_DELETE_END_POINT_USER).header(AUTHORIZATION_HEADER,
                Utils.getTokenForUser(mockMvc, objectMapper))).andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldDeletePostAsModerator() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(SHOULD_DELETE_END_POINT_MODERATOR).header(AUTHORIZATION_HEADER,
                Utils.getTokenForModerator(mockMvc, objectMapper))).andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    void shouldThrow404WhileDeletingAsModerator() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(THROW_NULL_END_POINT).header(AUTHORIZATION_HEADER,
                Utils.getTokenForModerator(mockMvc, objectMapper))).andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldEditAsUser() throws Exception {
        var editDTO = new EditDTO();
        editDTO.setTitle(SHOULD_EDIT_TITLE_USER);
        editDTO.setContent(SHOULD_EDIT_CONTENT);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(editDTO);
        mockMvc.perform(MockMvcRequestBuilders.patch(SHOULD_EDIT_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath(TITLE_JSON_PATH).value(SHOULD_EDIT_TITLE_USER))
                .andExpect(MockMvcResultMatchers.jsonPath(CONTENT_JSON_PATH).value(SHOULD_EDIT_CONTENT));
    }

    @Test
    void shouldThrowNoPermsWhileEditingAsUser() throws Exception {
        var editDTO = new EditDTO();
        editDTO.setTitle(SHOULD_EDIT_TITLE_USER);
        editDTO.setContent(SHOULD_EDIT_CONTENT);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(editDTO);
        mockMvc.perform(MockMvcRequestBuilders.patch(THROW_EDIT_END_POINT_USER)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldThrow400WhileEditingAsUser() throws Exception {
        var editDTO = new EditDTO();
        editDTO.setTitle(THROW_EDIT_TITLE);
        editDTO.setContent(SHOULD_EDIT_CONTENT);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(editDTO);
        mockMvc.perform(MockMvcRequestBuilders.patch(SHOULD_EDIT_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForUser(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldEditAsModerator() throws Exception {
        var editDTO = new EditDTO();
        editDTO.setTitle(SHOULD_EDIT_TITLE_MODERATOR);
        editDTO.setContent(SHOULD_EDIT_CONTENT);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(editDTO);
        mockMvc.perform(MockMvcRequestBuilders.patch(SHOULD_EDIT_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForModerator(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath(TITLE_JSON_PATH).value(SHOULD_EDIT_TITLE_MODERATOR))
                .andExpect(MockMvcResultMatchers.jsonPath(CONTENT_JSON_PATH).value(SHOULD_EDIT_CONTENT));
    }

    @Test
    void shouldThrow404WhileEditingAsModerator() throws Exception {
        var editDTO = new EditDTO();
        editDTO.setTitle(SHOULD_EDIT_TITLE_MODERATOR);
        editDTO.setContent(SHOULD_EDIT_CONTENT);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(editDTO);
        mockMvc.perform(MockMvcRequestBuilders.patch(THROW_NULL_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForModerator(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldThrow400WhileEditingAsModerator() throws Exception {
        var editDTO = new EditDTO();
        editDTO.setTitle(THROW_EDIT_TITLE);
        editDTO.setContent(SHOULD_EDIT_CONTENT);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(editDTO);
        mockMvc.perform(MockMvcRequestBuilders.patch(SHOULD_EDIT_END_POINT)
                        .header(AUTHORIZATION_HEADER, Utils.getTokenForModerator(mockMvc, objectMapper))
                        .contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldGiveLike() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(SHOULD_LIKE_END_POINT).header(AUTHORIZATION_HEADER,
                        Utils.getTokenForUser(mockMvc, objectMapper))).andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath(WHO_JSON_PATH).exists());
    }

    @Test
    void shouldThrow400WhileGivingLike() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(THROW_LIKE_END_POINT).header(AUTHORIZATION_HEADER,
                Utils.getTokenForUser(mockMvc, objectMapper))).andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldRemoveLike() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(SHOULD_UNLIKE_END_POINT).header(AUTHORIZATION_HEADER,
                Utils.getTokenForUser(mockMvc, objectMapper))).andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void shouldThrow404WhileRemovingLike() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(THROW_UNLIKE_END_POINT).header(AUTHORIZATION_HEADER,
                Utils.getTokenForUser(mockMvc, objectMapper))).andExpect(MockMvcResultMatchers.status().is(404));
    }

}

