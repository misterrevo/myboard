package com.revo.myboard.section.dto;

import com.revo.myboard.category.dto.ShortCategoryDTO;
import lombok.*;

import java.util.List;

@Data
@Builder
public class SectionDTO {

    private long id;
    private String name;
    private List<ShortCategoryDTO> categories;
}
