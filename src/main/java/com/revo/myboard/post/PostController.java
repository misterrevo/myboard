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

@RestController
@RequestMapping("/posts")
@Validated
@AllArgsConstructor
class PostController {

    private static final String POST_LOCATION = "/posts";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable long id, HttpServletRequest request) {
        var postDTO = postService.getPostDTOById(id);
        return ResponseEntity.ok(postDTO);
    }

    @GetMapping()
    public ResponseEntity<List<ShortPostDTO>> searchPostsByTitle(@RequestParam String title,
                                                                   HttpServletRequest request) {
        var postsDTO = postService.searchPostsByTitle(title);
        return ResponseEntity.ok(postsDTO);
    }

    @GetMapping("/last-active")
    public ResponseEntity<List<ShortPostDTO>> getLastActivePosts(HttpServletRequest request) {
        var postsDTO = postService.getTenLastActivitedPosts();
        return ResponseEntity.ok(postsDTO);
    }

    @GetMapping("/most-liked")
    public ResponseEntity<List<ShortPostDTO>> getMostLikedPosts(HttpServletRequest request) {
        var postsDTO = postService.getTenMostLikedPosts();
        return ResponseEntity.ok(postsDTO);
    }

    @PostMapping()
    @ForUser
    public ResponseEntity<PostDTO> createPost(@RequestHeader(AUTHORIZATION_HEADER) String token,
                                           @RequestBody @Valid CreateDTO createDTO, HttpServletRequest request) {
        var postDTO = postService.createPost(token, createDTO);
        return ResponseEntity.created(URI.create(POST_LOCATION)).body(postDTO);
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
        var postDTO = postService.editPostById(token, id, editDTO);
        return ResponseEntity.ok(postDTO);
    }

    @PatchMapping("/{id}/like")
    @ForUser
    public ResponseEntity<LikeDTO> giveLikeForPostById(@RequestHeader(AUTHORIZATION_HEADER) String token, @PathVariable long id,
                                                       HttpServletRequest request) {
        var likeDTO = postService.giveLikeForPostById(token, id);
        return ResponseEntity.ok(likeDTO);
    }

    @PatchMapping("/{id}/unlike")
    @ForUser
    public ResponseEntity<LikeDTO> removeLikeFromPostById(@RequestHeader(AUTHORIZATION_HEADER) String token, @PathVariable long id,
                                                         HttpServletRequest request) {
        var likeDTO = postService.removeLikeFromPostById(token, id);
        return ResponseEntity.ok(likeDTO);
    }
}
