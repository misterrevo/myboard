package com.revo.myboard.section;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    private static final String TEST_NAME = "TEST";
    private static final String TEST_NAME_NEW = "NEW NAME";
    @Mock
    private SectionRepository sectionRepository;

    @InjectMocks
    private SectionService sectionService;

    private Section testSection;

    @BeforeEach
    void init(){
        testSection = Section.builder()
                .id(1L)
                .name(TEST_NAME)
                .build();
    }

    @Test
    void createSection() {
        //given
        Mockito.when(sectionRepository.save(Mockito.any(Section.class))).thenReturn(testSection);
        //when
        var sectionDTO = sectionService.createSection(TEST_NAME);
        //then
        Assertions.assertEquals(sectionDTO.getId(), 1L);
        Assertions.assertEquals(sectionDTO.getName(), TEST_NAME);
        Assertions.assertEquals(sectionDTO.getCategories().size(), 0);
    }

    @Test
    void getSectionDTOById() {
        //given
        Mockito.when(sectionRepository.findById(1L)).thenReturn(Optional.of(testSection));
        //when
        var sectionDTO = sectionService.getSectionDTOById(1L);
        //then
        Assertions.assertEquals(sectionDTO.getId(), 1L);
        Assertions.assertEquals(sectionDTO.getName(), TEST_NAME);
        Assertions.assertEquals(sectionDTO.getCategories().size(), 0);
    }

    @Test
    void renameSectionById() {
        //given
        Mockito.when(sectionRepository.findById(1L)).thenReturn(Optional.of(testSection));
        //when
        var sectionDTO = sectionService.renameSectionById(1, TEST_NAME_NEW);
        //then
        Assertions.assertEquals(sectionDTO.getId(), 1L);
        Assertions.assertEquals(sectionDTO.getName(), TEST_NAME_NEW);
        Assertions.assertEquals(sectionDTO.getCategories().size(), 0);
    }

    @Test
    void getAllSections() {
        //given
        Mockito.when(sectionRepository.findAll()).thenReturn(Arrays.asList(testSection));
        //when
        var sectionsDTO = sectionService.getAllSections();
        //then
        Assertions.assertEquals(sectionsDTO.size(), 1);
        Assertions.assertEquals(sectionsDTO.get(0).getCategories().size(), 0);
        Assertions.assertEquals(sectionsDTO.get(0).getName(), TEST_NAME);
        Assertions.assertEquals(sectionsDTO.get(0).getId(), 1L);
    }
}