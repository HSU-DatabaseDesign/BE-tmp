package com.example.WebNovelReviewSite.domain.badge.listener;

import com.example.WebNovelReviewSite.domain.badge.event.CollectionCreatedEvent;
import com.example.WebNovelReviewSite.domain.badge.event.FollowAddedEvent;
import com.example.WebNovelReviewSite.domain.badge.event.ReviewCreatedEvent;
import com.example.WebNovelReviewSite.domain.badge.event.ReviewLikedEvent;
import com.example.WebNovelReviewSite.domain.badge.event.UserLoggedInEvent;
import com.example.WebNovelReviewSite.domain.badge.service.BadgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class BadgeEventListener {

    private final BadgeService badgeService;

    @Async
    @EventListener
    @Transactional
    public void handleReviewCreated(ReviewCreatedEvent event) {
        log.info("리뷰 작성 이벤트 수신: userId={}", event.getUserId());
        badgeService.checkAndAssignReviewCountBadge(event.getUserId());
    }

    @Async
    @EventListener
    @Transactional
    public void handleCollectionCreated(CollectionCreatedEvent event) {
        log.info("컬렉션 생성 이벤트 수신: userId={}", event.getUserId());
        badgeService.checkAndAssignCollectionCountBadge(event.getUserId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleFollowAdded(FollowAddedEvent event) {
        log.info("팔로우 추가 이벤트 수신: userId={}", event.getUserId());
        badgeService.checkAndAssignFollowCountBadge(event.getUserId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleReviewLiked(ReviewLikedEvent event) {
        log.info("리뷰 좋아요 이벤트 수신: reviewAuthorId={}", event.getReviewAuthorId());
        badgeService.checkAndAssignLikeCountBadge(event.getReviewAuthorId());
    }

    @Async
    @EventListener
    @Transactional
    public void handleUserLoggedIn(UserLoggedInEvent event) {
        log.info("로그인 이벤트 수신: userId={}", event.getUserId());
        badgeService.checkAndAssignLoginDaysBadge(event.getUserId());
    }
}

