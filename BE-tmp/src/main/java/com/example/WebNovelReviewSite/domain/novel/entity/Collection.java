package com.example.WebNovelReviewSite.domain.novel.entity;

import com.example.WebNovelReviewSite.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "collection")
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_id")
    private Long collectionId;

    //user - collection
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "collection_name",length = 255)
    private String collectionName;

    @Column(name = "content",length = 255)
    private String content;

    //collection - collected_novel
    @OneToMany(mappedBy = "collection")
    private List<CollectedNovel> collectedNovels = new ArrayList<>();
}
