package com.example.WebNovelReviewSite.domain.user.repository;

import com.example.WebNovelReviewSite.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    boolean existsById(String id);

    boolean existsByEmail(String email);
}
