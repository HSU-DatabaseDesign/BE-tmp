package com.example.WebNovelReviewSite.domain.hashtag.entity;


import com.example.WebNovelReviewSite.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "review_hashtag")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReviewHashtag {

    @EmbeddedId
    private ReviewHashtagId reviewHashtagId;

    @MapsId("reviewId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="review_id",nullable = false)
    private Review review;

    @MapsId("hashtagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hashtag_id",nullable = false)
    private Hashtag hashtag;
}
