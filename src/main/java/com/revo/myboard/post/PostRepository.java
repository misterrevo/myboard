package com.revo.myboard.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
 * Created By Revo
 */

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByOrderByLastActiveDateDesc(Pageable pageable);

    List<Post> findByOrderByMyLikesDesc(Pageable pageable);

    Optional<Post> findByTitle(String title);

    boolean existsByTitle(String title);

    List<Post> findByTitleContaining(String title);

}
