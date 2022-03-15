package com.revo.myboard.category.dto;

import com.revo.myboard.category.Category;
import lombok.*;

/*
 * Created By Revo
 */

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class ShortCategoryDTO {

    private long id;
    private String name;

}
