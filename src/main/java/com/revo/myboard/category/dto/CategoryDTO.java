package com.revo.myboard.category.dto;

import com.revo.myboard.category.Category;
import com.revo.myboard.post.dto.ShortPostDTO;
import lombok.*;

import java.util.List;

/*
 * Created By Revo
 */

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public final class CategoryDTO {

    private long id;
    private String name;
    private List<ShortPostDTO> posts;
    private long section;

}
