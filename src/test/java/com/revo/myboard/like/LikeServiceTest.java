package com.revo.myboard.like;

import com.revo.myboard.comment.Comment;
import com.revo.myboard.comment.CommentServiceApi;
import com.revo.myboard.post.Post;
import com.revo.myboard.post.PostServiceApi;
import com.revo.myboard.user.User;
import com.revo.myboard.user.UserServiceApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    private static final String FAKE_TOKEN = "FAKE TOKEN";
    private static final String TEST_USERNAME = "TEST";

    @Mock
    private LikeRepository likeRepository;
    @Mock
    private UserServiceApi userService;
    @Mock
    private PostServiceApi postService;
    @Mock
    private CommentServiceApi commentService;

    @InjectMocks
    private LikeService likeService;

    private Like testLike;
    private User testUser;
    private Post testPost;
    private Comment testComment;

    @BeforeEach
    void init(){
        testLike = Like.builder()
                .who(User.builder()
                        .login(TEST_USERNAME)
                        .build())
                .build();
        testUser = User.builder()
                .login(TEST_USERNAME)
                .build();
        testPost = Post.builder()
                .id(1L)
                .build();
        testComment = Comment.builder()
                .id(1L)
                .build();
    }

    @Test
    void giveForPostById() {
        //given
        testLike.setPost(testPost);
        Mockito.when(likeRepository.save(Mockito.any(Like.class))).thenReturn(testLike);
        Mockito.when(userService.currentLoggedUser(Mockito.any(String.class))).thenReturn(testUser);
        Mockito.when(postService.getPostById(1L)).thenReturn(testPost);
        //when
        var likeDTO = likeService.giveForPostById(FAKE_TOKEN, 1L);
        //then
        Assertions.assertEquals(likeDTO.getPost(), 1L);
        Assertions.assertEquals(likeDTO.getWho(), TEST_USERNAME);
    }

    @Test
    void giveForCommentById() {
        //given
        testLike.setComment(testComment);
        Mockito.when(likeRepository.save(Mockito.any(Like.class))).thenReturn(testLike);
        Mockito.when(userService.currentLoggedUser(Mockito.any(String.class))).thenReturn(testUser);
        Mockito.when(commentService.getCommentById(1L)).thenReturn(testComment);
        //when
        var likeDTO = likeService.giveForCommentById(FAKE_TOKEN, 1L);
        //then
        Assertions.assertEquals(likeDTO.getComment(), 1L);
        Assertions.assertEquals(likeDTO.getWho(), TEST_USERNAME);
    }

    @Test
    void removeFromPostById() {
        //given
        testLike.setPost(testPost);
        testPost.getMyLikes().add(testLike);
        Mockito.when(userService.currentLoggedUser(Mockito.any(String.class))).thenReturn(testUser);
        Mockito.when(postService.getPostById(1L)).thenReturn(testPost);
        //when
        var likeDTO = likeService.removeFromPostById(FAKE_TOKEN, 1L);
        //then
        Assertions.assertEquals(likeDTO.getPost(), 1L);
        Assertions.assertEquals(likeDTO.getWho(), TEST_USERNAME);
    }

    @Test
    void removeFromCommentById() {
        //given
        testLike.setComment(testComment);
        testComment.getMyLikes().add(testLike);
        Mockito.when(userService.currentLoggedUser(Mockito.any(String.class))).thenReturn(testUser);
        Mockito.when(commentService.getCommentById(1L)).thenReturn(testComment);
        //when
        var likeDTO = likeService.removeFromCommentById(FAKE_TOKEN, 1L);
        //then
        Assertions.assertEquals(likeDTO.getComment(), 1L);
        Assertions.assertEquals(likeDTO.getWho(), TEST_USERNAME);
    }
}