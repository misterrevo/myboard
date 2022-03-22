package com.revo.myboard.category.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShortCategoryDTO {

    private long id;
    private String name;
}
