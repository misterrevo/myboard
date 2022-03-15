package com.revo.myboard.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * Created By Revo
 */

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
