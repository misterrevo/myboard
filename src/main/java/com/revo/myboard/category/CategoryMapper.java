package com.revo.myboard.category;

import com.revo.myboard.category.dto.CategoryDTO;
import com.revo.myboard.category.dto.ShortCategoryDTO;
import com.revo.myboard.post.Post;
import com.revo.myboard.post.PostMapper;
import com.revo.myboard.post.dto.ShortPostDTO;

import java.util.List;

public class CategoryMapper {

    public static CategoryDTO mapCategoryDTOFromCategory(Category category) {
        return buildCategoryDTO(category);
    }

    private static CategoryDTO buildCategoryDTO(Category category){
        var section = category.getSection();
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .posts(mapFromList(category.getPosts()))
                .section(section.getId())
                .build();
    }

    private static List<ShortPostDTO> mapFromList(List<Post> posts) {
        return posts.stream()
                .map(PostMapper::mapShortPostDTOFromPost)
                .toList();
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
