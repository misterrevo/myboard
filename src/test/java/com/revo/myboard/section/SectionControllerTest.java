package com.revo.myboard.section;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revo.myboard.Utils;
import com.revo.myboard.section.dto.NameDTO;
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
public class SectionControllerTest {

    private static final String SHOULD_GET_END_POINT = "/sections/1";
    private static final String THROW_NULL_END_POINT = "/sections/-1";

    private static final String SHOULD_GET_ALL = "/sections";

    private static final String ROLE = "ADMIN";
    private static final String CREATE_END_POINT = "/sections";
    private static final String SHOULD_CREATE_NAME = "sectionCreate";
    private static final String THROW_CREATE_NAME = "testGet";

    private static final String SHOULD_DELETE_END_POINT = "/sections/2";

    private static final String SHOULD_RENAME_END_POINT = "/sections/3";
    private static final String THROW_INVALID_RENAME_END_POINT = "/sections/4";
    private static final String SHOULD_RENAME_NAME = "testRenameChanged";
    private static final String THROW_INVALID_RENAME_NAME = "testRename400";
    private static final String THROW_NULL_RENAME_NAME = "testRename404";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetSection() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SHOULD_GET_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void shouldThrow404WhileGetting() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(THROW_NULL_END_POINT)).andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldGetAllSections() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SHOULD_GET_ALL)).andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    @WithMockUser(roles = ROLE)
    void shouldCreateSection() throws Exception {
        var nameDTO = new NameDTO();
        nameDTO.setName(SHOULD_CREATE_NAME);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(nameDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(CREATE_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(SHOULD_CREATE_NAME));
    }

    @Test
    @WithMockUser(roles = ROLE)
    void shouldThrow400WhileCreating() throws Exception {
        var nameDTO = new NameDTO();
        nameDTO.setName(THROW_CREATE_NAME);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(nameDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(CREATE_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    @WithMockUser(roles = ROLE)
    void shouldDeleteSection() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(SHOULD_DELETE_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    @WithMockUser(roles = ROLE)
    void shouldThrow404WhileDeleting() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(THROW_NULL_END_POINT))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    @WithMockUser(roles = ROLE)
    void shouldRenameSection() throws Exception {
        var nameDTO = new NameDTO();
        nameDTO.setName(SHOULD_RENAME_NAME);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(nameDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(SHOULD_RENAME_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(SHOULD_RENAME_NAME));
    }

    @Test
    @WithMockUser(roles = ROLE)
    void shouldThrow400WhileRenaming() throws Exception {
        var nameDTO = new NameDTO();
        nameDTO.setName(THROW_INVALID_RENAME_NAME);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(nameDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(THROW_INVALID_RENAME_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    @WithMockUser(roles = ROLE)
    void shouldThrow404WhileRenaming() throws Exception {
        var nameDTO = new NameDTO();
        nameDTO.setName(THROW_NULL_RENAME_NAME);
        String json = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(nameDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch(THROW_NULL_END_POINT).contentType(Utils.APPLICATION_JSON_UTF8).content(json))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

}

