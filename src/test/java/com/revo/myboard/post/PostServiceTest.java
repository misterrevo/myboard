package com.revo.myboard.post;

import com.revo.myboard.category.Category;
import com.revo.myboard.category.CategoryServiceApi;
import com.revo.myboard.group.Authority;
import com.revo.myboard.group.Group;
import com.revo.myboard.post.dto.CreateDTO;
import com.revo.myboard.post.dto.EditDTO;
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
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    private static final String TEST_NAME = "TEST";
    private static final String FAKE_TOKEN = "FAKE TOKEN";
    private static final String TEST_NAME_NEW = "NEW NAME";
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserServiceApi userService;
    @Mock
    private CategoryServiceApi categoryService;

    @InjectMocks
    private PostService postService;

    private Post testPost;
    private User testUser;
    private User testModerator;
    private Category testCategory;
    private CreateDTO testCreateDTO;
    private EditDTO testEditDTO;

    @BeforeEach
    void init(){
        testUser = User.builder()
                .login(TEST_NAME)
                .group(Group.builder()
                        .authority(Authority.USER)
                        .build())
                .build();
        testModerator = User.builder()
                .login(TEST_NAME)
                .group(Group.builder()
                        .authority(Authority.MODERATOR)
                        .build())
                .build();
        testCategory = Category.builder()
                .id(1L)
                .build();
        testPost = Post.builder()
                .id(1L)
                .author(testUser)
                .title(TEST_NAME)
                .content(TEST_NAME)
                .date(LocalDateTime.now())
                .lastEditDate(LocalDateTime.now())
                .lastActiveDate(LocalDateTime.now())
                .category(testCategory)
                .build();
        testCreateDTO = new CreateDTO();
        testCreateDTO.setContent(TEST_NAME);
        testCreateDTO.setTitle(TEST_NAME);
        testCreateDTO.setCategory(1L);
        testEditDTO = new EditDTO();
        testEditDTO.setTitle(TEST_NAME_NEW);
        testEditDTO.setContent(TEST_NAME_NEW);
    }

    @Test
    void createPost() {
        //given
        Mockito.when(postRepository.existsByTitle(Mockito.any(String.class))).thenReturn(false);
        Mockito.when(postRepository.save(Mockito.any(Post.class))).thenReturn(testPost);
        Mockito.when(userService.currentLoggedUser(Mockito.any(String.class))).thenReturn(testUser);
        Mockito.when(categoryService.getCategoryById(1L)).thenReturn(testCategory);
        //when
        var postDTO = postService.createPost(FAKE_TOKEN, testCreateDTO);
        //then
        Assertions.assertEquals(postDTO.getId(), 1L);
        Assertions.assertEquals(postDTO.getUser(), TEST_NAME);
        Assertions.assertEquals(postDTO.getTitle(), TEST_NAME);
        Assertions.assertEquals(postDTO.getContent(), TEST_NAME);
        Assertions.assertEquals(postDTO.getComments().size(), 0);
        Assertions.assertNotNull(postDTO.getDate());
        Assertions.assertNotNull(postDTO.getLastEditDate());
        Assertions.assertEquals(postDTO.getCategory(), 1L);
        Assertions.assertEquals(postDTO.getReports().size(), 0);
        Assertions.assertEquals(postDTO.getLikes().size(), 0);
    }

    @Test
    void searchPostsByTitle() {
        //given
        Mockito.when(postRepository.findByTitleContaining(Mockito.any(String.class))).thenReturn(List.of(testPost));
        //when
        var searchesDTO = postService.searchPostsByTitle(TEST_NAME);
        //then
        Assertions.assertEquals(searchesDTO.size(), 1);
        Assertions.assertEquals(searchesDTO.get(0).getTitle(), TEST_NAME);
        Assertions.assertEquals(searchesDTO.get(0).getId(), 1L);
        Assertions.assertEquals(searchesDTO.get(0).getAnswers(), 0);
        Assertions.assertEquals(searchesDTO.get(0).getAuthor(), TEST_NAME);
        Assertions.assertNotNull(searchesDTO.get(0).getLastActivity());
    }

    @Test
    void getPostDTOById() {
        //given
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        //when
        var postDTO = postService.getPostDTOById(1L);
        //then
        Assertions.assertEquals(postDTO.getId(), 1L);
        Assertions.assertEquals(postDTO.getUser(), TEST_NAME);
        Assertions.assertEquals(postDTO.getTitle(), TEST_NAME);
        Assertions.assertEquals(postDTO.getContent(), TEST_NAME);
        Assertions.assertEquals(postDTO.getComments().size(), 0);
        Assertions.assertNotNull(postDTO.getDate());
        Assertions.assertNotNull(postDTO.getLastEditDate());
        Assertions.assertEquals(postDTO.getCategory(), 1L);
        Assertions.assertEquals(postDTO.getReports().size(), 0);
        Assertions.assertEquals(postDTO.getLikes().size(), 0);
    }

    @Test
    void editPostByIdAsUser() {
        //given
        testUser.getPosts().add(testPost);
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        Mockito.when(userService.currentLoggedUser(Mockito.any(String.class))).thenReturn(testUser);
        //when
        var postDTO = postService.editPostById(FAKE_TOKEN, 1L, testEditDTO);
        //then
        Assertions.assertEquals(postDTO.getId(), 1L);
        Assertions.assertEquals(postDTO.getUser(), TEST_NAME);
        Assertions.assertEquals(postDTO.getTitle(), TEST_NAME_NEW);
        Assertions.assertEquals(postDTO.getContent(), TEST_NAME_NEW);
        Assertions.assertEquals(postDTO.getComments().size(), 0);
        Assertions.assertNotNull(postDTO.getDate());
        Assertions.assertNotNull(postDTO.getLastEditDate());
        Assertions.assertEquals(postDTO.getCategory(), 1L);
        Assertions.assertEquals(postDTO.getReports().size(), 0);
        Assertions.assertEquals(postDTO.getLikes().size(), 0);
    }

    @Test
    void editPostByIdAsModerator() {
        //given
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        Mockito.when(userService.currentLoggedUser(Mockito.any(String.class))).thenReturn(testModerator);
        //when
        var postDTO = postService.editPostById(FAKE_TOKEN, 1L, testEditDTO);
        //then
        Assertions.assertEquals(postDTO.getId(), 1L);
        Assertions.assertEquals(postDTO.getUser(), TEST_NAME);
        Assertions.assertEquals(postDTO.getTitle(), TEST_NAME_NEW);
        Assertions.assertEquals(postDTO.getContent(), TEST_NAME_NEW);
        Assertions.assertEquals(postDTO.getComments().size(), 0);
        Assertions.assertNotNull(postDTO.getDate());
        Assertions.assertNotNull(postDTO.getLastEditDate());
        Assertions.assertEquals(postDTO.getCategory(), 1L);
        Assertions.assertEquals(postDTO.getReports().size(), 0);
        Assertions.assertEquals(postDTO.getLikes().size(), 0);
    }

    @Test
    void getTenLastActivitedPosts() {
        //given
        Mockito.when(postRepository.findByOrderByLastActiveDateDesc(Mockito.any(Pageable.class))).thenReturn(Arrays.asList(testPost));
        //when
        var shortPostsDTO = postService.getTenLastActivitedPosts();
        //then
        Assertions.assertEquals(shortPostsDTO.size(), 1);
        Assertions.assertEquals(shortPostsDTO.get(0).getAnswers(), 0);
        Assertions.assertEquals(shortPostsDTO.get(0).getId(), 1L);
        Assertions.assertEquals(shortPostsDTO.get(0).getAuthor(), TEST_NAME);
        Assertions.assertEquals(shortPostsDTO.get(0).getTitle(), TEST_NAME);
        Assertions.assertNotNull(shortPostsDTO.get(0).getLastActivity());
}

    @Test
    void getTenMostLikedPosts() {
        //given
        Mockito.when(postRepository.findByOrderByMyLikesDesc(Mockito.any(Pageable.class))).thenReturn(Arrays.asList(testPost));
        //when
        var shortPostsDTO = postService.getTenMostLikedPosts();
        //then
        Assertions.assertEquals(shortPostsDTO.size(), 1);
        Assertions.assertEquals(shortPostsDTO.get(0).getAnswers(), 0);
        Assertions.assertEquals(shortPostsDTO.get(0).getId(), 1L);
        Assertions.assertEquals(shortPostsDTO.get(0).getAuthor(), TEST_NAME);
        Assertions.assertEquals(shortPostsDTO.get(0).getTitle(), TEST_NAME);
        Assertions.assertNotNull(shortPostsDTO.get(0).getLastActivity());
    }
}