package com.revo.myboard.section.dto;

import com.revo.myboard.category.dto.ShortCategoryDTO;
import com.revo.myboard.section.Section;
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
public class SectionDTO {

    private long id;
    private String name;
    private List<ShortCategoryDTO> categories;

}
