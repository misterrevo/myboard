package com.revo.myboard.category;

import com.revo.myboard.category.dto.CreateDTO;
import com.revo.myboard.section.Section;
import com.revo.myboard.section.SectionServiceApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    private static final String TEST_NAME = "TEST";
    private static final String TEST_NAME_NEW = "NEW";

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SectionServiceApi sectionServiceApi;

    @InjectMocks
    private CategoryService categoryService;

    private Section testSection;
    private Category testCategory;
    private CreateDTO testCreateDTO;

    @BeforeEach
    void init(){
        testSection = Section.builder()
                .id(1L)
                .name(TEST_NAME)
                .build();
        testCategory = Category.builder()
                .id(1)
                .name(TEST_NAME)
                .section(testSection)
                .build();
        testCreateDTO = new CreateDTO();
        testCreateDTO.setName("TEST");
        testCreateDTO.setSection(1L);
    }

    @Test
    void createCategory() {
        //given
        Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(testCategory);
        Mockito.when(sectionServiceApi.getSectionById(1L)).thenReturn(testSection);
        //when
        var categoryDTO = categoryService.createCategory(testCreateDTO);
        //then
        Assertions.assertEquals(categoryDTO.getId(), 1L);
        Assertions.assertEquals(categoryDTO.getName(), TEST_NAME);
        Assertions.assertEquals(categoryDTO.getSection(), 1L);
        Assertions.assertNotNull(categoryDTO.getPosts());
    }

    @Test
    void getCategoryDTOById() {
        //given
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        //when
        var categoryDTO = categoryService.getCategoryDTOById(1L);
        //then
        Assertions.assertEquals(categoryDTO.getId(), 1L);
        Assertions.assertEquals(categoryDTO.getName(), TEST_NAME);
        Assertions.assertEquals(categoryDTO.getSection(), 1L);
        Assertions.assertNotNull(categoryDTO.getPosts());
    }

    @Test
    void renameCategoryById() {
        //given
        Mockito.when(categoryRepository.existsByName(Mockito.any(String.class))).thenReturn(false);
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        //when
        var categoryDTO = categoryService.renameCategoryById(1L, TEST_NAME_NEW);
        //then
        Assertions.assertEquals(categoryDTO.getId(), 1L);
        Assertions.assertEquals(categoryDTO.getName(), TEST_NAME_NEW);
        Assertions.assertEquals(categoryDTO.getSection(), 1L);
        Assertions.assertNotNull(categoryDTO.getPosts());
    }
}