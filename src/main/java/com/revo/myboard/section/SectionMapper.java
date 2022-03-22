package com.revo.myboard.section;

import com.revo.myboard.category.Category;
import com.revo.myboard.category.CategoryMapper;
import com.revo.myboard.category.dto.ShortCategoryDTO;
import com.revo.myboard.section.dto.SectionDTO;

import java.util.List;

public final class SectionMapper {

    public static SectionDTO mapFromSection(Section section) {
        return buildSectionDTO(section);
    }

    private static SectionDTO buildSectionDTO(Section section){
        return SectionDTO.builder()
                .id(section.getId())
                .name(section.getName())
                .categories(mapFromCategories(section.getCategories()))
                .build();
    }

    private static List<ShortCategoryDTO> mapFromCategories(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::mapShortCategoryDTOFromCategory)
                .toList();
    }
}
