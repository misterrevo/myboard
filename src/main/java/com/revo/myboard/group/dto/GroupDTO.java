package com.revo.myboard.group.dto;

import lombok.*;



@Data
@Builder
public class GroupDTO {

    private long id;
    private String name;
    private String authority;
}
