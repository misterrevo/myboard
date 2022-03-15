package com.revo.myboard.comment;

import com.revo.myboard.comment.dto.CommentDTO;
import com.revo.myboard.comment.dto.ContentDTO;
import com.revo.myboard.comment.dto.CreateDTO;
import com.revo.myboard.like.dto.LikeDTO;
import com.revo.myboard.security.annotation.ForUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

/*
 *  Created By Revo
 */

@RestController
@RequestMapping("/comments")
@Validated
@AllArgsConstructor
public class CommentController {

    private static final String LOCATION = "/comments";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable long id, HttpServletRequest request) {
        return ResponseEntity.ok(commentService.getCommentDTOById(id));
    }

    @PostMapping()
    @ForUser
    public ResponseEntity<CommentDTO> createComment(@RequestHeader(AUTHORIZATION_HEADER) String token, @RequestBody @Valid CreateDTO createDTO,
                                           HttpServletRequest request) {
        return ResponseEntity.created(URI.create(LOCATION)).body(commentService.createComment(token, createDTO.getPost(), createDTO.getContent()));
    }

    @PatchMapping("/{id}")
    @ForUser
    public ResponseEntity<CommentDTO> editCommentById(@RequestHeader(AUTHORIZATION_HEADER) String token, @PathVariable long id,
                                @RequestBody @Valid ContentDTO editDTO, HttpServletRequest request) {
        var commentDTO = commentService.editCommentById(token, id, editDTO.getNewContent());
        return ResponseEntity.ok(commentDTO);
    }

    @DeleteMapping("/{id}")
    @ForUser
    public ResponseEntity<Void> deleteCommentById(@RequestHeader(AUTHORIZATION_HEADER) String token, @PathVariable long id,
                                        HttpServletRequest request) {
        commentService.deleteCommentById(token, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/like")
    @ForUser
    public ResponseEntity<LikeDTO> giveLikeForCommentById(@RequestHeader(AUTHORIZATION_HEADER) String token, @PathVariable long id,
                                                          HttpServletRequest request) {
        return ResponseEntity.created(URI.create(LOCATION)).body(commentService.giveLikeForCommentById(token, id));
    }

    @DeleteMapping("/{id}/unlike")
    @ForUser
    public ResponseEntity<Void> removeLikeFromCommentById(@RequestHeader(AUTHORIZATION_HEADER) String token, @PathVariable long id,
                                          HttpServletRequest request) {
        commentService.removeLikeFromCommentById(token, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
