package com.revo.myboard.post;

import com.revo.myboard.like.dto.LikeDTO;
import com.revo.myboard.post.dto.CreateDTO;
import com.revo.myboard.post.dto.EditDTO;
import com.revo.myboard.post.dto.PostDTO;
import com.revo.myboard.post.dto.ShortPostDTO;
import com.revo.myboard.security.annotation.ForUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/*
 *  Created By Revo
 */

@RestController
@RequestMapping("/posts")
@Validated
@AllArgsConstructor
public class PostController {

    private static final String LOCATION = "/posts";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable long id, HttpServletRequest request) {
        return ResponseEntity.ok(postService.getPostDTOById(id));
    }

    @GetMapping("/{title}/search")
    public ResponseEntity<List<ShortPostDTO>> searchPostsByTitle(@PathVariable String title,
                                                                   HttpServletRequest request) {
        return ResponseEntity.ok(postService.searchPostsByTitle(title));
    }

    @GetMapping("/last-active")
    public ResponseEntity<List<ShortPostDTO>> getLastActivePosts(HttpServletRequest request) {
        return ResponseEntity.ok(postService.getTenLastActivitedPosts());
    }

    @GetMapping("/most-liked")
    public ResponseEntity<List<ShortPostDTO>> getMostLikedPosts(HttpServletRequest request) {
        return ResponseEntity.ok(postService.getTenMostLikedPosts());
    }

    @PostMapping()
    @ForUser
    public ResponseEntity<PostDTO> createPost(@RequestHeader(AUTHORIZATION_HEADER) String token,
                                           @RequestBody @Valid CreateDTO createDTO, HttpServletRequest request) {
        return ResponseEntity.created(URI.create(LOCATION)).body(postService.createPost(token, createDTO.getCategory(), createDTO.getTitle(), createDTO.getContent()));
    }

    @DeleteMapping("/{id}")
    @ForUser
    public ResponseEntity<Void> deletePostById(@RequestHeader(AUTHORIZATION_HEADER) String token, @PathVariable long id,
                               HttpServletRequest request) {
        postService.deletePostById(token, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    @ForUser
    public ResponseEntity<PostDTO> editPostById(@RequestHeader(AUTHORIZATION_HEADER) String token, @PathVariable long id,
                             @RequestBody @Valid EditDTO editDTO, HttpServletRequest request) {
        var postDTO = postService.editPostById(token, id, editDTO.getTitle(), editDTO.getContent());
        return ResponseEntity.ok(postDTO);
    }

    @PostMapping("/{id}/like")
    @ForUser
    public ResponseEntity<LikeDTO> giveLikeForPostById(@RequestHeader(AUTHORIZATION_HEADER) String token, @PathVariable long id,
                                                       HttpServletRequest request) {
        return ResponseEntity.created(URI.create(LOCATION)).body(postService.giveLikeForPostById(token, id));
    }

    @DeleteMapping("/{id}/unlike")
    @ForUser
    public ResponseEntity<Void> removeLikeFromPostById(@RequestHeader(AUTHORIZATION_HEADER) String token, @PathVariable long id,
                                                         HttpServletRequest request) {
        postService.removeLikeFromPostById(token, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
