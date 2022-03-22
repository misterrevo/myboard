package com.revo.myboard.group;

import com.revo.myboard.group.dto.*;
import com.revo.myboard.security.annotation.ForAdmin;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
@ForAdmin
@Validated
@AllArgsConstructor
class GroupController {

    private static final String GROUP_LOCATION = "/groups";

    private final GroupService groupService;

    @GetMapping()
    public ResponseEntity<List<GroupDTO>> getAllGroups(HttpServletRequest request) {
        var groupsDTO = groupService.getAllGroups();
        return ResponseEntity.ok(groupsDTO);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<String>> getAuthoritiesList(HttpServletRequest request) {
        var authorityList = groupService.getAuthorityList();
        return ResponseEntity.ok(authorityList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> getGroupById(@PathVariable long id, HttpServletRequest request) {
        var groupDTO = groupService.getGroupDTOById(id);
        return ResponseEntity.ok(groupDTO);
    }

    @PostMapping()
    public ResponseEntity<GroupDTO> createGroup(@RequestBody @Valid CreateDTO createDTO, HttpServletRequest request) {
        var groupDTO = groupService.createGroup(createDTO);
        return ResponseEntity.created(URI.create(GROUP_LOCATION)).body(groupDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupById(@PathVariable long id, HttpServletRequest request) {
        groupService.deleteGroupById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/rename")
    public ResponseEntity<GroupDTO> renameGroupById(@PathVariable long id, @RequestBody @Valid NameDTO editNameDTO,
                                HttpServletRequest request) {
        var groupDTO = groupService.renameGroupById(id, editNameDTO.getNewName());
        return ResponseEntity.ok(groupDTO);
    }

    @PatchMapping("/{id}/authority")
    public ResponseEntity<GroupDTO> changeGroupAuthorityById(@PathVariable long id, @RequestBody @Valid AuthortiyDTO editAuthorityDTO,
                                     HttpServletRequest request) {
        var groupDTO = groupService.changeGroupAuthority(id, editAuthorityDTO.getNewAuthority());
        return ResponseEntity.ok(groupDTO);
    }
}
