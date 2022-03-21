package com.revo.myboard.report;

import com.revo.myboard.comment.Comment;
import com.revo.myboard.comment.CommentServiceApi;
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
class ReportServiceTest {

    private static final String TEST_NAME = "TEST";
    private static final String FAKE_TOKEN = "FAKE TOKEN";
    @Mock
    private ReportRepository reportRepository;
    @Mock
    private UserServiceApi userService;
    @Mock
    private PostServiceApi postService;
    @Mock
    private CommentServiceApi commentService;

    @InjectMocks
    private ReportService reportService;

    private Report testReportForComment;
    private Report testReportForPost;
    private User testUser;
    private User testModerator;
    private Post testPost;
    private Comment testComment;

    @BeforeEach
    void init(){
        testReportForComment = Report.builder()
                .id(1L)
                .checked(false)
                .content(TEST_NAME)
                .date(LocalDateTime.now())
                .reporter(User.builder()
                        .login(TEST_NAME)
                        .build())
                .comment(Comment.builder()
                        .id(1L)
                        .build())
                .build();
        testReportForPost = Report.builder()
                .id(1L)
                .checked(false)
                .content(TEST_NAME)
                .date(LocalDateTime.now())
                .reporter(User.builder()
                        .login(TEST_NAME)
                        .build())
                .post(Post.builder()
                        .id(1L)
                        .title(TEST_NAME)
                        .build())
                .build();
        testUser = User.builder()
                .group(Group.builder()
                        .authority(Authority.USER)
                        .build())
                .login(TEST_NAME)
                .build();
        testModerator = User.builder()
                .group(Group.builder()
                        .authority(Authority.MODERATOR)
                        .build())
                .login(TEST_NAME)
                .build();
        testPost = Post.builder()
                .id(1L)
                .title(TEST_NAME)
                .build();
        testComment = Comment.builder()
                .id(1L)
                .build();
    }

    @Test
    void getReportDTOByIdAsUserForComment() {
        //given
        testUser.getReports().add(testReportForComment);
        Mockito.when(reportRepository.findById(1L)).thenReturn(Optional.of(testReportForComment));
        Mockito.when(userService.currentLoggedUser(Mockito.any(String.class))).thenReturn(testUser);
        //when
        var reportDTO = reportService.getReportDTOById(FAKE_TOKEN, 1L);
        //then
        Assertions.assertEquals(reportDTO.getReporter(), TEST_NAME);
        Assertions.assertEquals(reportDTO.getComment(), 1L);
        Assertions.assertEquals(reportDTO.getId(), 1L);
        Assertions.assertNotNull(reportDTO.getDate());
        Assertions.assertFalse(reportDTO.isChecked());
    }

    @Test
    void getReportDTOByIdAsModeratorForComment() {
        //given
        Mockito.when(reportRepository.findById(1L)).thenReturn(Optional.of(testReportForComment));
        Mockito.when(userService.currentLoggedUser(Mockito.any(String.class))).thenReturn(testModerator);
        //when
        var reportDTO = reportService.getReportDTOById(FAKE_TOKEN, 1L);
        //then
        Assertions.assertEquals(reportDTO.getReporter(), TEST_NAME);
        Assertions.assertEquals(reportDTO.getComment(), 1L);
        Assertions.assertEquals(reportDTO.getId(), 1L);
        Assertions.assertNotNull(reportDTO.getDate());
        Assertions.assertFalse(reportDTO.isChecked());
    }

    @Test
    void getReportDTOByIdAsUserForPost() {
        //given
        testUser.getReports().add(testReportForPost);
        Mockito.when(reportRepository.findById(1L)).thenReturn(Optional.of(testReportForPost));
        Mockito.when(userService.currentLoggedUser(Mockito.any(String.class))).thenReturn(testUser);
        //when
        var reportDTO = reportService.getReportDTOById(FAKE_TOKEN, 1L);
        //then
        Assertions.assertEquals(reportDTO.getReporter(), TEST_NAME);
        Assertions.assertEquals(reportDTO.getPost(), 1L);
        Assertions.assertEquals(reportDTO.getPostTitle(), TEST_NAME);
        Assertions.assertEquals(reportDTO.getId(), 1L);
        Assertions.assertNotNull(reportDTO.getDate());
        Assertions.assertFalse(reportDTO.isChecked());
    }

    @Test
    void getReportDTOByIdAsModeratorForPost() {
        //given
        Mockito.when(reportRepository.findById(1L)).thenReturn(Optional.of(testReportForPost));
        Mockito.when(userService.currentLoggedUser(Mockito.any(String.class))).thenReturn(testModerator);
        //when
        var reportDTO = reportService.getReportDTOById(FAKE_TOKEN, 1L);
        //then
        Assertions.assertEquals(reportDTO.getReporter(), TEST_NAME);
        Assertions.assertEquals(reportDTO.getPost(), 1L);
        Assertions.assertEquals(reportDTO.getPostTitle(), TEST_NAME);
        Assertions.assertEquals(reportDTO.getId(), 1L);
        Assertions.assertNotNull(reportDTO.getDate());
        Assertions.assertFalse(reportDTO.isChecked());
    }

    @Test
    void createReportForPost() {
        //given
        Mockito.when(reportRepository.save(Mockito.any(Report.class))).thenReturn(testReportForPost);
        Mockito.when(userService.currentLoggedUser(Mockito.any(String.class))).thenReturn(testUser);
        Mockito.when(postService.getPostById(1L)).thenReturn(testPost);
        //when
        var reportDTO = reportService.createReportForPost(FAKE_TOKEN, 1L, TEST_NAME);
        //then
        Assertions.assertEquals(reportDTO.getReporter(), TEST_NAME);
        Assertions.assertEquals(reportDTO.getPost(), 1L);
        Assertions.assertEquals(reportDTO.getPostTitle(), TEST_NAME);
        Assertions.assertEquals(reportDTO.getId(), 1L);
        Assertions.assertNotNull(reportDTO.getDate());
        Assertions.assertFalse(reportDTO.isChecked());
    }

    @Test
    void createReportForComment() {
        //given
        Mockito.when(reportRepository.save(Mockito.any(Report.class))).thenReturn(testReportForComment);
        Mockito.when(userService.currentLoggedUser(Mockito.any(String.class))).thenReturn(testUser);
        Mockito.when(commentService.getCommentById(1L)).thenReturn(testComment);
        //when
        var reportDTO = reportService.createReportForComment(FAKE_TOKEN, 1L, TEST_NAME);
        //then
        Assertions.assertEquals(reportDTO.getReporter(), TEST_NAME);
        Assertions.assertEquals(reportDTO.getComment(), 1L);
        Assertions.assertEquals(reportDTO.getId(), 1L);
        Assertions.assertNotNull(reportDTO.getDate());
        Assertions.assertFalse(reportDTO.isChecked());
    }

    @Test
    void getAllNotCheckedReports() {
        //given
        Mockito.when(reportRepository.findByCheckedFalse()).thenReturn(Arrays.asList(testReportForComment));
        //when
        var reportsDTO = reportService.getAllNotCheckedReports();
        //then
        Assertions.assertEquals(reportsDTO.size(), 1);
        Assertions.assertEquals(reportsDTO.get(0).getReporter(), TEST_NAME);
        Assertions.assertEquals(reportsDTO.get(0).getComment(), 1L);
        Assertions.assertEquals(reportsDTO.get(0).getId(), 1L);
        Assertions.assertNotNull(reportsDTO.get(0).getDate());
        Assertions.assertFalse(reportsDTO.get(0).isChecked());
    }

    @Test
    void setReportAsChecked() {
        //given
        Mockito.when(reportRepository.findById(1L)).thenReturn(Optional.of(testReportForComment));
        //when
        var reportDTO = reportService.setReportAsChecked(1L);
        //then
        Assertions.assertEquals(reportDTO.getReporter(), TEST_NAME);
        Assertions.assertEquals(reportDTO.getComment(), 1L);
        Assertions.assertEquals(reportDTO.getId(), 1L);
        Assertions.assertNotNull(reportDTO.getDate());
        Assertions.assertTrue(reportDTO.isChecked());
    }
}