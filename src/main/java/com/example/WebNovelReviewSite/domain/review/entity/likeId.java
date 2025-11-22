package com.example.WebNovelReviewSite.domain.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

//복합 키 클래스
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class likeId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "review_id")
    private Long reviewId;
}
