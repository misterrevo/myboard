package com.revo.myboard.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revo.myboard.Utils;
import com.revo.myboard.category.dto.CreateDTO;
import com.revo.myboard.category.dto.NameDTO;
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
@WithMockUser(roles = {"ADMIN"})
public class CategoryControllerTest {

    private static final String CREATE_END_POINT = "/categories";
    private static final String SHOULD_CREATE_NAME = "testCategory";
    private static final String THROW_CREATE_NAME = "testCategory404";
    private static final String THROW_INVALID_CREATE_NAME = "testGet";

    private static final String SHOULD_GET_END_POINT = "/categories/1";
    private static final String THROW_NULL_END_POINT = "/categories/-1";

    private static final String SHOULD_DELETE_END_POINT = "/categories/2";

    private static final String SHOULD_RENAME_END_POINT = "/categories/3";

    private static final String SHOULD_RENAME_NAME = "testRenameChanged";
    private static final String THROW_INVALID_RENAME_END_POINT = "/categories/4";
    private static final String THROW_RENAME_NAME = "testRename400";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateCategory() throws Exception {
        var createDTO = new CreateDTO();
        createDTO.setName(SHOULD_CREATE_NAME);
        createDTO.setSection(1);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(CREATE_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(SHOULD_CREATE_NAME));
    }

    @Test
    void shouldThrow400WhileCreating() throws Exception {
        var createDTO = new CreateDTO();
        createDTO.setName(THROW_INVALID_CREATE_NAME);
        createDTO.setSection(1);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(CREATE_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void shouldThrow404WhileCreating() throws Exception {
        var createDTO = new CreateDTO();
        createDTO.setName(THROW_CREATE_NAME);
        createDTO.setSection(1);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(CREATE_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldGetCategory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SHOULD_GET_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void shouldThrow404WhileGetting() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(THROW_NULL_END_POINT)).andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(SHOULD_DELETE_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    void shouldThrow404WhileDeleting() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(THROW_NULL_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldRenameCategory() throws Exception {
        var nameDTO = new NameDTO();
        nameDTO.setNewName(SHOULD_RENAME_NAME);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(nameDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(SHOULD_RENAME_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(SHOULD_RENAME_NAME));
    }

    @Test
    void shouldThrow404WhileRenaming() throws Exception {
        var nameDTO = new NameDTO();
        nameDTO.setNewName(SHOULD_RENAME_NAME);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(nameDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(THROW_NULL_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldThrow400WhileRenaming() throws Exception {
        var nameDTO = new NameDTO();
        nameDTO.setNewName(THROW_RENAME_NAME);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(nameDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(THROW_INVALID_RENAME_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

}
