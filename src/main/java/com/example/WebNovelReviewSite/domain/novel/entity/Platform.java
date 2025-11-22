package com.example.WebNovelReviewSite.domain.novel.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "platform")
public class Platform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "platform_id")
    private Long platformId;

    @Column(name = "platform_name",length = 20)
    private String platformName;

    @Column(name = "platform_image", length = 255)
    private String platformImage;

    //platform - novel_platform
    @OneToMany(mappedBy = "platform")
    private List<Novel> novels;
}
