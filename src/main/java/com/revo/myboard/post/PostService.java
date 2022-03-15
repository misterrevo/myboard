package com.revo.myboard.post;

import com.revo.myboard.category.Category;
import com.revo.myboard.category.CategoryService;
import com.revo.myboard.exception.NoPermissionException;
import com.revo.myboard.exception.PostNameInUseException;
import com.revo.myboard.exception.PostNotExistsException;
import com.revo.myboard.group.Authority;
import com.revo.myboard.like.LikeService;
import com.revo.myboard.like.dto.LikeDTO;
import com.revo.myboard.post.dto.PostDTO;
import com.revo.myboard.post.dto.ShortPostDTO;
import com.revo.myboard.user.User;
import com.revo.myboard.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Created By Revo
 */

@Service
@Transactional
@AllArgsConstructor
public class PostService {

    private final PostRepository repository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final LikeService likeService;

    PostDTO createPost(String token, long category_id, String title, String content) {
        if (existsByTitle(title)) {
            throw new PostNameInUseException(title);
        }
        getUser(token).setLastActivityDate(LocalDateTime.now());
        return mapFromPost(repository.save(buildPost(token, category_id, title, content)));
    }

    private boolean existsByTitle(String title){
        return repository.existsByTitle(title);
    }

    private User getUser(String token){
        return userService.currentLoggedUser(token);
    }

    private PostDTO mapFromPost(Post post){
        return PostMapper.mapPostDTOFromPost(post);
    }

    private Post buildPost(String token, long category_id, String title, String content){
        return Post.builder().author(getUser(token))
                .category(getCategory(category_id))
                .content(content)
                .date(LocalDateTime.now())
                .lastActiveDate(LocalDateTime.now())
                .lastEditDate(LocalDateTime.now())
                .title(title)
                .build();
    }

    private Category getCategory(long id){
        return categoryService.getCategoryById(id);
    }

    List<ShortPostDTO> searchPostsByTitle(String title) {
        var result = findByTitleContaining(title);
        return mapFromList(result);
    }

    private List<Post> findByTitleContaining(String title){
        return repository.findByTitleContaining(title);
    }

    private List<ShortPostDTO> mapFromList(List<Post> post){
        return post.stream().map(this::mapFromPostAsShort).collect(Collectors.toList());
    }

    private ShortPostDTO mapFromPostAsShort(Post post){
        return PostMapper.mapShortPostDTOFromPost(post);
    }

    public Post getPostById(long id) {
        return repository.getById(id);
    }

    PostDTO getPostDTOById(long id) {
        return mapFromPost(getPostById(id));
    }

    PostDTO editPostById(String token, long id, String title, String content) {
        var user = getUser(token);
        if (user.getGroup().getAuthority().equals(Authority.USER) && !user.getPosts().contains(getPostById(id))) {
            throw new NoPermissionException(user.getLogin());
        }
        var post = getPostById(id);
        post.setContent(content);
        post.setTitle(title);
        post.setLastEditDate(LocalDateTime.now());
        return mapFromPost(post);
    }

    LikeDTO giveLikeForPostById(String token, long id) {
        return likeService.giveForPostById(token, id);
    }

    void removeLikeFromPostById(String token, long id) {
        likeService.removeFromPostById(token, id);
    }

    void deletePostById(String token, long id) {
        var user = getUser(token);
        if (user.getGroup().getAuthority().equals(Authority.USER)) {
            if (user.getPosts().stream().noneMatch(post -> post.getId() == id)) {
                throw new PostNotExistsException(id);
            }
        }
        repository.delete(getPostById(id));
    }

    List<ShortPostDTO> getTenLastActivitedPosts() {
        var result = findLastActivitated();
        return mapFromList(result);
    }

    private List<Post> findLastActivitated(){
        return repository.findByOrderByLastActiveDateDesc(PageRequest.of(0, 10));
    }

    List<ShortPostDTO> getTenMostLikedPosts() {
        var result = findMostLiked();
        return mapFromList(result);
    }

    private List<Post> findMostLiked(){
        return repository.findByOrderByMyLikesDesc(PageRequest.of(0, 10));
    }

}
