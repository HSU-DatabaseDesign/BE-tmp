package com.example.WebNovelReviewSite.domain.hashtag.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

//복합 키 클래스
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class NovelHashtagId {
    @Column(name = "novel_id")
    private Long novelId;

    @Column(name="hashtag_id")
    private Long hashtagId;
}
