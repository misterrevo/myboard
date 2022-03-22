package com.revo.myboard.group;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revo.myboard.Utils;
import com.revo.myboard.group.dto.AuthortiyDTO;
import com.revo.myboard.group.dto.CreateDTO;
import com.revo.myboard.group.dto.NameDTO;
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
@WithMockUser(roles = "ADMIN")
public class GroupControllerTest {

    private static final String SHOULD_GET_AUTHORITIES_END_POINT = "/groups/roles";

    private static final String SHOULD_GET_ALL_GROUPS_END_POINT = "/groups";

    private static final String SHOULD_GET_GROUP_END_POINT = "/groups/1";
    private static final String THROW_NULL_END_POINT = "/groups/-1";

    private static final String CREATE_END_POINT = "/groups";
    private static final String SHOULD_CREATE_AUTHORITY = "ADMIN";
    private static final String SHOULD_CREATE_NAME = "groupCreate";
    private static final String THROW_CREATE_NAME = "groupGet";

    private static final String SHOULD_DELETE_END_POINT = "/groups/2";

    private static final String SHOULD_RENAME_END_POINT = "/groups/3/rename";
    private static final String THROW_INVALID_RENAME_END_POINT = "/groups/4/rename";
    private static final String THROW_NULL_RENAME_END_POINT = "/groups/-1/rename";
    private static final String SHOULD_RENAME_NAME = "groupRenameChanged";
    private static final String THROW_RENAME_NAME = "groupRename400";

    private static final String SHOULD_CHANGE_END_POINT = "/groups/2/authority";
    private static final String THROW_CHANGE_END_POINT = "/groups/-1/authority";
    private static final String SHOULD_CHANGE_NAME = "USER";
    private static final String THROW_CHANGE_NAME = "USERERROR";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAuthorities() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SHOULD_GET_AUTHORITIES_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists());
    }

    @Test
    void shouldGetAllGroups() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SHOULD_GET_ALL_GROUPS_END_POINT)).andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists());
    }

    @Test
    void shouldGetGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SHOULD_GET_GROUP_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void shouldThrow404WhileGetting() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(THROW_NULL_END_POINT)).andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldCreateGroup() throws Exception {
        var createDTO = new CreateDTO();
        createDTO.setAuthority(SHOULD_CREATE_AUTHORITY);
        createDTO.setName(SHOULD_CREATE_NAME);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(CREATE_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(SHOULD_CREATE_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authority").value(SHOULD_CREATE_AUTHORITY));
    }

    @Test
    void shouldThrow400WhileCreating() throws Exception {
        var createDTO = new CreateDTO();
        createDTO.setAuthority(SHOULD_CREATE_AUTHORITY);
        createDTO.setName(THROW_CREATE_NAME);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(CREATE_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldDeleteGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(SHOULD_DELETE_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    void shoulThrow404WhileDeleting() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(THROW_NULL_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldRenameGroup() throws Exception {
        var nameDTO = new NameDTO();
        nameDTO.setNewName(SHOULD_RENAME_NAME);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(nameDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(SHOULD_RENAME_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(SHOULD_RENAME_NAME));
    }

    @Test
    void shouldThrow400WhileRenaming() throws Exception {
        var nameDTO = new NameDTO();
        nameDTO.setNewName(THROW_RENAME_NAME);
        nameDTO.setNewName(THROW_RENAME_NAME);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(nameDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(THROW_INVALID_RENAME_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldThrow404WhileRenaming() throws Exception {
        var nameDTO = new NameDTO();
        nameDTO.setNewName(SHOULD_RENAME_NAME);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(nameDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(THROW_NULL_RENAME_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldChangeAuthorityGroup() throws Exception {
        var authorityDTO = new AuthortiyDTO();
        authorityDTO.setNewAuthority(SHOULD_CHANGE_NAME);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(authorityDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(SHOULD_CHANGE_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authority").value(SHOULD_CHANGE_NAME));
    }

    @Test
    void shouldThrow400WhileChangingAuthority() throws Exception {
        var authorityDTO = new AuthortiyDTO();
        authorityDTO.setNewAuthority(THROW_CHANGE_NAME);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(authorityDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(SHOULD_CHANGE_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldThrow404WhileChangingAuthority() throws Exception {
        var authorityDTO = new AuthortiyDTO();
        authorityDTO.setNewAuthority(SHOULD_CHANGE_NAME);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(authorityDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(THROW_CHANGE_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

}

