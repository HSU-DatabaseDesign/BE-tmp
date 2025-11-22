package com.example.WebNovelReviewSite.domain.review.repository;

import com.example.WebNovelReviewSite.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByNovel_NovelId(Long novelId);

    boolean existsByUser_UserIdAndNovel_NovelId(Long userId, Long novelId);
}
