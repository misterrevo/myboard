package com.revo.myboard.group;

import com.revo.myboard.group.dto.GroupDTO;

/*
 * Created By Revo
 */

public final class GroupMapper {

    public static GroupDTO mapFromGroup(Group group) {
        return buildGroupDTO(group);
    }

    private static GroupDTO buildGroupDTO(Group group) {
        return GroupDTO.builder()
                .id(group.getId())
                .authority(group.getAuthority().toString())
                .name(group.getName())
                .build();
    }

}
