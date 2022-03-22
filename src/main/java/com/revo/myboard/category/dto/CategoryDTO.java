package com.revo.myboard.category.dto;

import com.revo.myboard.post.dto.ShortPostDTO;
import lombok.*;

import java.util.List;

@Data
@Builder
public class CategoryDTO {

    private long id;
    private String name;
    private List<ShortPostDTO> posts;
    private long section;
}
