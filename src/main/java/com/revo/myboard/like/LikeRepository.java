package com.revo.myboard.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface LikeRepository extends JpaRepository<Like, Long> {
}