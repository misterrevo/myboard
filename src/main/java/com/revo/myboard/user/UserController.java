package com.revo.myboard.user;

import com.revo.myboard.security.annotation.ForAdmin;
import com.revo.myboard.security.annotation.ForModerator;
import com.revo.myboard.security.annotation.ForUser;
import com.revo.myboard.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/*
 * Created By Revo
 */

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final UserService userService;
    @Value("${spring.security.jwt.secret}")
    private String secret;

    @GetMapping("/{login}")
    public ResponseEntity<UserDTO> getUserByLogin(@PathVariable String login, HttpServletRequest request) {
        return ResponseEntity.ok(userService.getUserDTOByLogin(login));
    }

    @GetMapping("/{login}/search")
    public ResponseEntity<List<SearchDTO>> getUsersByContent(@PathVariable String login, HttpServletRequest request) {
        return ResponseEntity.ok(userService.searchUsersByLogin(login));
    }

    @PatchMapping("/password-reset")
    @ForUser
    public ResponseEntity<UserDTO> changeUserPassword(@RequestHeader(AUTHORIZATION_HEADER) String token,
                                                      @RequestBody @Valid PasswordDTO passwordDTO, HttpServletRequest request) {
        var userDTO = userService.changeUserPassword(token, passwordDTO.getPassword());
        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping("/email-reset")
    @ForUser
    public ResponseEntity<UserDTO> changeUserEmail(@RequestHeader(AUTHORIZATION_HEADER) String token, @RequestBody @Valid EmailDTO emailDTO,
                                HttpServletRequest request) {
        var userDTO = userService.changeUserEmail(token, emailDTO.getEmail());
        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping("/data-reset")
    @ForUser
    public ResponseEntity<UserDTO> changeUserData(@RequestHeader(AUTHORIZATION_HEADER) String token, @RequestBody @Valid DataDTO dataDTO,
                               HttpServletRequest request) {
        var userDTO = userService.changeUserData(token, dataDTO.getDescription(), dataDTO.getAge(), dataDTO.getCity(),
                dataDTO.getPage(), dataDTO.getGender());
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping()
    @ForUser
    public ResponseEntity<ProfileDTO> currentLoggedUserProfile(@RequestHeader(AUTHORIZATION_HEADER) String token,
                                                               HttpServletRequest request) {
        return ResponseEntity.ok(userService.currentLoggedUserProfileDTO(token));
    }

    @DeleteMapping("/{login}")
    @ForUser
    public ResponseEntity<Void> deleteUserByLogin(@RequestHeader(AUTHORIZATION_HEADER) String token, @PathVariable String login, HttpServletRequest request) {
        userService.deleteUserByLogin(token, login);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/genders")
    @ForUser
    public ResponseEntity<List<String>> getSexList(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getSexList());
    }

    @PatchMapping("/{login}/ban")
    @ForModerator
    public ResponseEntity<UserDTO> banUserByLogin(@PathVariable String login, HttpServletRequest request) {
        var userDTO = userService.banUserByLogin(login);
        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping("/{login}/unban")
    @ForModerator
    public ResponseEntity<UserDTO> unbanUserByLogin(@PathVariable String login, HttpServletRequest request) {
        var userDTO = userService.unbanUserByLogin(login);
        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping("/{login}/{group_id}")
    @ForAdmin
    public ResponseEntity<UserDTO> setUserGroupByLogin(@PathVariable String login, @PathVariable long group_id, HttpServletRequest request) {
        var userDTO = userService.setGroupByLogin(login, group_id);
        return ResponseEntity.ok(userDTO);
    }

}
