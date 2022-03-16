package com.revo.myboard.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
 * Created By Revo
 */

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    boolean existsByName(String name);

    Optional<Image> findByName(String name);

}
