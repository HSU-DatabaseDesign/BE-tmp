package com.example.WebNovelReviewSite.domain.hashtag.entity;

import com.example.WebNovelReviewSite.domain.novel.entity.Novel;
import com.example.WebNovelReviewSite.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name ="hashtag")
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long hashtagId;

    @Column(name = "hashtag_name",length = 10)
    private String hashtagName;

    //hashtag- review_hashtag
    @OneToMany(mappedBy = "hashtag")
    private List<ReviewHashtag> reviewHashtags = new ArrayList<>();

    //hashtag - novel_hashtag
    @OneToMany(mappedBy = "hashtag")
    private List<NovelHashtag> novelHashtags = new ArrayList<>();
}
