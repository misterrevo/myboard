package com.revo.myboard.group;

import com.revo.myboard.group.dto.GroupDTO;

public final class GroupMapper {

    public static GroupDTO mapFromGroup(Group group) {
        return buildGroupDTO(group);
    }

    private static GroupDTO buildGroupDTO(Group group) {
        var authority = group.getAuthority();
        return GroupDTO.builder()
                .id(group.getId())
                .authority(authority.toString())
                .name(group.getName())
                .build();
    }
}
