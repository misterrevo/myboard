package com.revo.myboard.post;

import com.revo.myboard.category.Category;
import com.revo.myboard.category.CategoryServiceApi;
import com.revo.myboard.exception.NoPermissionException;
import com.revo.myboard.exception.PostNameInUseException;
import com.revo.myboard.exception.PostNotExistsException;
import com.revo.myboard.group.Authority;
import com.revo.myboard.like.LikeServiceApi;
import com.revo.myboard.like.dto.LikeDTO;
import com.revo.myboard.post.dto.CreateDTO;
import com.revo.myboard.post.dto.EditDTO;
import com.revo.myboard.post.dto.PostDTO;
import com.revo.myboard.post.dto.ShortPostDTO;
import com.revo.myboard.user.User;
import com.revo.myboard.user.UserServiceApi;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@AllArgsConstructor
class PostService implements PostServiceApi {

    private final PostRepository repository;
    private final UserServiceApi userService;
    private final CategoryServiceApi categoryService;
    private final LikeServiceApi likeService;

    PostDTO createPost(String token, CreateDTO createDTO) {
        var title = createDTO.getTitle();
        if (existsByTitle(title)) {
            throw new PostNameInUseException(title);
        }
        var category_id = createDTO.getCategory();
        var content = createDTO.getContent();
        getUser(token).setLastActivityDate(LocalDateTime.now());
        var savedPost = repository.save(buildPost(token, category_id, title, content));
        return mapFromPost(savedPost);
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
        return Post.builder()
                .author(getUser(token))
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
        return post.stream()
                .map(this::mapFromPostAsShort)
                .toList();
    }

    private ShortPostDTO mapFromPostAsShort(Post post){
        return PostMapper.mapShortPostDTOFromPost(post);
    }

    @Override
    public Post getPostById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PostNotExistsException(id));
    }

    PostDTO getPostDTOById(long id) {
        return mapFromPost(getPostById(id));
    }

    PostDTO editPostById(String token, long id, EditDTO editDTO) {
        var user = getUser(token);
        if (hasUserRole(user) && isNotOwnerOfPost(user, id)) {
            throw new NoPermissionException(user.getLogin());
        }
        var title = editDTO.getTitle();
        var content = editDTO.getContent();
        var post = getPostById(id);
        post.setContent(content);
        post.setTitle(title);
        post.setLastEditDate(LocalDateTime.now());
        return mapFromPost(post);
    }

    private boolean isNotOwnerOfPost(User user, long id) {
        var posts = user.getPosts();
        return !posts.contains(getPostById(id));
    }

    private boolean hasUserRole(User user) {
        return Objects.equals(user.getGroup().getAuthority(), Authority.USER);
    }

    LikeDTO giveLikeForPostById(String token, long id) {
        return likeService.giveForPostById(token, id);
    }

    LikeDTO removeLikeFromPostById(String token, long id) {
        return likeService.removeFromPostById(token, id);
    }

    void deletePostById(String token, long id) {
        var user = getUser(token);
        if (hasUserRole(user) && isNotOwnerOfPost(user, id)) {
            throw new NoPermissionException(user.getLogin());
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
