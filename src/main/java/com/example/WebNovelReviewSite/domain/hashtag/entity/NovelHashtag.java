package com.example.WebNovelReviewSite.domain.hashtag.entity;

import com.example.WebNovelReviewSite.domain.novel.entity.Novel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "novel_hashtag")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NovelHashtag {

    @EmbeddedId
    private NovelHashtagId novelHashtagId;

    @MapsId("novelId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="novel_id",nullable = false)
    private Novel novel;

    @MapsId("hashtagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="hashtag_id",nullable = false)
    private Hashtag hashtag;
}
