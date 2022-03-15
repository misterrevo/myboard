package com.revo.myboard.section;

/*
 * Created By Revo
 */

import com.revo.myboard.category.CategoryMapper;
import com.revo.myboard.section.dto.SectionDTO;

import java.util.ArrayList;
import java.util.stream.Collectors;

public final class SectionMapper {

    public static SectionDTO mapFromSection(Section section) {
        if(section.getCategories() != null){
            return buildSectionDTO(section);
        }
        return buildNewSectionDTO(section);
    }

    private static SectionDTO buildNewSectionDTO(Section section){
        return SectionDTO.builder()
                .id(section.getId())
                .name(section.getName())
                .categories(new ArrayList<>())
                .build();
    }

    private static SectionDTO buildSectionDTO(Section section){
        return SectionDTO.builder()
                .id(section.getId())
                .name(section.getName())
                .categories(section.getCategories().stream()
                        .map(CategoryMapper::mapShortCategoryDTOFromCategory).collect(Collectors.toList()))
                .build();
    }

}
