package com.revo.myboard.user;

import com.revo.myboard.security.dto.RegisterDTO;
import com.revo.myboard.user.dto.UserDTO;

import java.util.List;

public interface UserServiceApi {
    List<User> getAll();

    UserDTO registerUser(RegisterDTO registerDTO);

    UserDTO resendActivationCode(String email);

    UserDTO activeUserByCode(String code);

    User currentLoggedUser(String token);

    User getUserByLogin(String login);

    UserDTO activeByLogin(String login);
}
