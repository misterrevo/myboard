package com.revo.myboard.category;

import com.revo.myboard.category.dto.CategoryDTO;
import com.revo.myboard.category.dto.ShortCategoryDTO;
import com.revo.myboard.post.PostMapper;

import java.util.ArrayList;

/*
 * Created By Revo
 */

public final class CategoryMapper {

    public static CategoryDTO mapCategoryDTOFromCategory(Category category) {
        if(category.getPosts() != null){
            return buildForCategory(category);
        }
        return buildForNewCategory(category);
    }

    private static CategoryDTO buildForNewCategory(Category category){
        return CategoryDTO.builder().id(category.getId()).name(category.getName())
                .posts(new ArrayList<>())
                .section(category.getSection().getId()).build();
    }

    private static CategoryDTO buildForCategory(Category category){
        return CategoryDTO.builder().id(category.getId()).name(category.getName())
                .posts(category.getPosts().stream().map(PostMapper::mapShortPostDTOFromPost).toList())
                .section(category.getSection().getId()).build();
    }

    public static ShortCategoryDTO mapShortCategoryDTOFromCategory(Category category) {
        return buildShortCategoryDTO(category);
    }

    private static ShortCategoryDTO buildShortCategoryDTO(Category category) {
        return ShortCategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
