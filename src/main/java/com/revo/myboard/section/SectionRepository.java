package com.revo.myboard.section;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SectionRepository extends JpaRepository<Section, Long> {

    boolean existsByName(String name);
}