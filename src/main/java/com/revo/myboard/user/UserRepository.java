package com.revo.myboard.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
 * Created By Revo
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    Optional<User> findByEmail(String email);

    Optional<User> findByCode(String code);

    List<User> findByLoginContaining(String login);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

}
