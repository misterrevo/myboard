package com.revo.myboard.group.dto;

import lombok.*;

/*
 * Created By Revo
 */

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class GroupDTO {

    private long id;
    private String name;
    private String authority;

}
