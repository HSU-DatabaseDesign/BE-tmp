package com.example.WebNovelReviewSite.domain.novel.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "novel_platform")
public class NovelPlatform {

    @EmbeddedId
    private NovelPlatformId id;

    @MapsId("novelId")
    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "novel_id",nullable = false)
    private Novel novel;

    @MapsId("platformId")
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="platform_id",nullable = false)
    private Platform platform;
}
