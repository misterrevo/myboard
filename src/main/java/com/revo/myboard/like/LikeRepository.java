package com.revo.myboard.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * Created By Revo
 */

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
}