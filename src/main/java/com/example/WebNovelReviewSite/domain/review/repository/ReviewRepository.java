package com.example.WebNovelReviewSite.domain.review.repository;

import com.example.WebNovelReviewSite.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByNovel_NovelId(Long novelId);

    boolean existsByUser_UserIdAndNovel_NovelId(Long userId, Long novelId);

    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.reviewId = :reviewId")
    Optional<Review> findByIdWithUser(@Param("reviewId") Long reviewId);

    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.user.userId = :userId")
    List<Review> findByUserIdWithUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(rl) FROM Review r JOIN r.userList rl WHERE r.user.userId = :userId")
    Long countTotalLikesByUserId(@Param("userId") Long userId);
}
