package com.example.WebNovelReviewSite.domain.hashtag.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.CollectionId;

//복합 키 클래스
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class ReviewHashtagId {

    @Column(name="review_id")
    private Long reviewId;

    @Column(name="hashtag_id")
    private Long hashtagId;
}
