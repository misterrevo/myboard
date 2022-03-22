package com.revo.myboard.user.dto;

import com.revo.myboard.post.dto.ShortPostDTO;
import lombok.*;

import java.util.List;

@Data
@Builder
public class UserDTO {

    private String login;
    private String group;
    private String email;
    private DataDTO data;
    private List<ShortPostDTO> posts;
    private boolean blocked;
    private boolean active;
}
