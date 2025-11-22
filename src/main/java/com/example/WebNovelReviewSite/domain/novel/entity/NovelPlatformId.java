package com.example.WebNovelReviewSite.domain.novel.entity;

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
public class NovelPlatformId implements Serializable {

    @Column(name = "novel_id")
    private Long novelId;

    @Column(name = "platform_id")
    private Long platformId;
}
