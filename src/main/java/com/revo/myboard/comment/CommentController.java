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

@RestController
@RequestMapping("/comments")
@Validated
@AllArgsConstructor
class CommentController {

    private static final String COMMENT_LOCATION = "/comments";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable long id, HttpServletRequest request) {
        var commentDTO = commentService.getCommentDTOById(id);
        return ResponseEntity.ok(commentDTO);
    }

    @PostMapping()
    @ForUser
    public ResponseEntity<CommentDTO> createComment(@RequestHeader(AUTHORIZATION_HEADER) String token, @RequestBody @Valid CreateDTO createDTO,
                                           HttpServletRequest request) {
        var commentDTO = commentService.createComment(token, createDTO);
        return ResponseEntity.created(URI.create(COMMENT_LOCATION)).body(commentDTO);
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

    @PatchMapping("/{id}/like")
    @ForUser
    public ResponseEntity<LikeDTO> giveLikeForCommentById(@RequestHeader(AUTHORIZATION_HEADER) String token, @PathVariable long id,
                                                          HttpServletRequest request) {
        var likeDTO = commentService.giveLikeForCommentById(token, id);
        return ResponseEntity.ok(likeDTO);
    }

    @PatchMapping("/{id}/unlike")
    @ForUser
    public ResponseEntity<LikeDTO> removeLikeFromCommentById(@RequestHeader(AUTHORIZATION_HEADER) String token, @PathVariable long id,
                                          HttpServletRequest request) {
        var likeDTO = commentService.removeLikeFromCommentById(token, id);
        return ResponseEntity.ok(likeDTO);
    }
}
