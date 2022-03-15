package com.revo.myboard.user.dto;

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
public class UserDTO {

    private String login;
    private String group;
    private String email;
    private DataDTO data;
    private List<ShortPostDTO> posts;
    private boolean blocked;
    private boolean active;

}
