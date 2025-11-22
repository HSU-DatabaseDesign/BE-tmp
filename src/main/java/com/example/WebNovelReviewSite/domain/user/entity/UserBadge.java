package com.example.WebNovelReviewSite.domain.user.entity;

import com.example.WebNovelReviewSite.domain.badge.entity.Badge;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_badge")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserBadge {

    //복합키 클래스를 PK로 같는 엔티티
    @EmbeddedId
    private UserBadgeId id;

    @MapsId("userId") //복합키 클래스의 필드명
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("badgeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", nullable = false)
    private Badge badge;
}
