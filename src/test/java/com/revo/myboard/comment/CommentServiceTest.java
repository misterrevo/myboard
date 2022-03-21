package com.revo.myboard.comment;

import com.revo.myboard.comment.dto.CreateDTO;
import com.revo.myboard.group.Authority;
import com.revo.myboard.group.Group;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    private static final String TEST_NAME = "TEST";
    private static final String FAKE_TOKEN = "FAKE TOKEN";
    private static final String TEST_CONTENT_NEW = "NEW TEST CONTENT";

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserServiceApi userService;
    @Mock
    private PostServiceApi postService;

    @InjectMocks
    private CommentService commentService;

    private Comment testComment;
    private User testUser;
    private User testModerator;
    private Group testUserGroup;
    private Group testModeratorGroup;
    private Post testPost;
    private CreateDTO testCreateDTO;

    @BeforeEach
    void init(){
        testModerator = User.builder()
                .login(TEST_NAME)
                .group( Group.builder()
                        .name(TEST_NAME)
                        .authority(Authority.MODERATOR)
                        .build())
                .build();
        testUser = User.builder()
                .login(TEST_NAME)
                .group(Group.builder()
                        .name(TEST_NAME)
                        .authority(Authority.USER)
                        .build())
                .build();
        testPost = Post.builder()
                .id(1L)
                .build();
        testComment = Comment.builder()
                .id(1L)
                .author(testUser)
                .content(TEST_NAME)
                .date(LocalDateTime.now())
                .lastEditDate(LocalDateTime.now())
                .post(testPost)
                .build();
        testCreateDTO = new CreateDTO();
        testCreateDTO.setPost(1L);
        testCreateDTO.setContent(TEST_NAME);
    }

    @Test
    void createComment() {
        //given
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(testComment);
        Mockito.when(postService.getPostById(1L)).thenReturn(testPost);
        Mockito.when(userService.currentLoggedUser(Mockito.any(String.class))).thenReturn(testUser);
        //when
        var commentDTO = commentService.createComment(FAKE_TOKEN, testCreateDTO);
        //then
        Assertions.assertEquals(commentDTO.getContent(), TEST_NAME);
        Assertions.assertEquals(commentDTO.getId(), 1L);
        Assertions.assertEquals(commentDTO.getPost(), 1L);
        Assertions.assertEquals(commentDTO.getUser(), TEST_NAME);
        Assertions.assertNotNull(commentDTO.getDate());
        Assertions.assertNotNull(commentDTO.getLastEditDate());
        Assertions.assertEquals(commentDTO.getLikes().size(), 0);
        Assertions.assertEquals(commentDTO.getReports().size(), 0);
    }

    @Test
    void getCommentDTOById() {
        //given
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        //when
        var commentDTO = commentService.getCommentDTOById(1L);
        //then
        Assertions.assertEquals(commentDTO.getContent(), TEST_NAME);
        Assertions.assertEquals(commentDTO.getId(), 1L);
        Assertions.assertEquals(commentDTO.getPost(), 1L);
        Assertions.assertEquals(commentDTO.getUser(), TEST_NAME);
        Assertions.assertNotNull(commentDTO.getDate());
        Assertions.assertNotNull(commentDTO.getLastEditDate());
        Assertions.assertEquals(commentDTO.getLikes().size(), 0);
        Assertions.assertEquals(commentDTO.getReports().size(), 0);
    }

    @Test
    void editCommentByIdAsUser() {
        //given
        testUser.setComments(Arrays.asList(testComment));
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        Mockito.when(userService.currentLoggedUser(Mockito.any(String.class))).thenReturn(testUser);
        //when
        var commentDTO = commentService.editCommentById(FAKE_TOKEN, 1L, TEST_CONTENT_NEW);
        //then
        Assertions.assertEquals(commentDTO.getContent(), TEST_CONTENT_NEW);
        Assertions.assertEquals(commentDTO.getId(), 1L);
        Assertions.assertEquals(commentDTO.getPost(), 1L);
        Assertions.assertEquals(commentDTO.getUser(), TEST_NAME);
        Assertions.assertNotNull(commentDTO.getDate());
        Assertions.assertNotNull(commentDTO.getLastEditDate());
        Assertions.assertEquals(commentDTO.getLikes().size(), 0);
        Assertions.assertEquals(commentDTO.getReports().size(), 0);
    }

    @Test
    void editCommentByIdAsModerator() {
        //given
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        Mockito.when(userService.currentLoggedUser(Mockito.any(String.class))).thenReturn(testModerator);
        //when
        var commentDTO = commentService.editCommentById(FAKE_TOKEN, 1L, TEST_CONTENT_NEW);
        //then
        Assertions.assertEquals(commentDTO.getContent(), TEST_CONTENT_NEW);
        Assertions.assertEquals(commentDTO.getId(), 1L);
        Assertions.assertEquals(commentDTO.getPost(), 1L);
        Assertions.assertEquals(commentDTO.getUser(), TEST_NAME);
        Assertions.assertNotNull(commentDTO.getDate());
        Assertions.assertNotNull(commentDTO.getLastEditDate());
        Assertions.assertEquals(commentDTO.getLikes().size(), 0);
        Assertions.assertEquals(commentDTO.getReports().size(), 0);
    }
}