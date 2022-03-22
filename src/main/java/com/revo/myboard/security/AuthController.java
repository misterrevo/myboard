package com.revo.myboard.security;

import com.revo.myboard.security.annotation.ForAdmin;
import com.revo.myboard.security.dto.CodeDTO;
import com.revo.myboard.security.dto.CredentialsDTO;
import com.revo.myboard.security.dto.RegisterDTO;
import com.revo.myboard.user.UserServiceApi;
import com.revo.myboard.user.dto.EmailDTO;
import com.revo.myboard.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@RestController
@Validated
@AllArgsConstructor
class AuthController {

    private static final String USER_LOCATION = "/users";

    private final UserServiceApi userService;

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody @Valid CredentialsDTO credentialsDTO, HttpServletRequest request) { return ResponseEntity.ok().build(); }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody @Valid RegisterDTO registerDTO, HttpServletRequest request) {
        var userDTO = userService.registerUser(registerDTO);
        return ResponseEntity.created(URI.create(USER_LOCATION)).body(userDTO);
    }

    @PatchMapping("/active")
    public ResponseEntity<UserDTO> activeUserByCode(@RequestBody @Valid CodeDTO codeDTO, HttpServletRequest request) {
        var userDTO = userService.activeUserByCode(codeDTO.getCode());
        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping("/active/{login}")
    @ForAdmin
    public ResponseEntity<UserDTO> activeUserByLogin(@PathVariable String login, HttpServletRequest request) {
        var userDTO = userService.activeByLogin(login);
        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping("/resend-code")
    public ResponseEntity<UserDTO> resenedActivationCode(@RequestBody @Valid EmailDTO emailDTO, HttpServletRequest request) {
        var userDTO = userService.resendActivationCode(emailDTO.getEmail());
        return ResponseEntity.ok(userDTO);
    }
}
