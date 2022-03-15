package com.revo.myboard.security;

import com.revo.myboard.security.annotation.ForAdmin;
import com.revo.myboard.security.dto.CodeDTO;
import com.revo.myboard.security.dto.CredentialsDTO;
import com.revo.myboard.security.dto.RegisterDTO;
import com.revo.myboard.user.UserService;
import com.revo.myboard.user.dto.EmailDTO;
import com.revo.myboard.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

/*
 * Created By Revo
 */

@RestController
@Validated
@AllArgsConstructor
public class AuthController {

    private static final String LOCATION = "/users";

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody @Valid CredentialsDTO credentialsDTO, HttpServletRequest request) { return ResponseEntity.ok().build(); }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody @Valid RegisterDTO registerDTO, HttpServletRequest request) {
        return ResponseEntity.created(URI.create(LOCATION)).body(userService.registerUser(registerDTO.getLogin(), registerDTO.getPassword(), registerDTO.getEmail()));
    }

    @PatchMapping("/active")
    public ResponseEntity<UserDTO> activeUserByCode(@RequestBody @Valid CodeDTO codeDTO, HttpServletRequest request) {
        var userDTO = userService.activeUserByCode(codeDTO.getCode());
        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping("/{login}")
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
