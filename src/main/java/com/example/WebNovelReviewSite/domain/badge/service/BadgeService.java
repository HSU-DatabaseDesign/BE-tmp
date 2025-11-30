package com.example.WebNovelReviewSite.domain.badge.service;

import com.example.WebNovelReviewSite.domain.badge.dto.BadgeRequestDTO;
import com.example.WebNovelReviewSite.domain.badge.dto.BadgeResponseDTO;
import com.example.WebNovelReviewSite.domain.badge.entity.Badge;
import com.example.WebNovelReviewSite.domain.badge.entity.UserBadge;
import com.example.WebNovelReviewSite.domain.badge.entity.UserBadgeId;
import com.example.WebNovelReviewSite.domain.badge.enums.BadgeType;
import com.example.WebNovelReviewSite.domain.badge.repository.BadgeRepository;
import com.example.WebNovelReviewSite.domain.badge.repository.UserBadgeRepository;
import com.example.WebNovelReviewSite.domain.novel.repository.CollectionRepository;
import com.example.WebNovelReviewSite.domain.review.entity.Review;
import com.example.WebNovelReviewSite.domain.review.repository.ReviewRepository;
import com.example.WebNovelReviewSite.domain.user.entity.User;
import com.example.WebNovelReviewSite.domain.user.repository.FollowRepository;
import com.example.WebNovelReviewSite.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final CollectionRepository collectionRepository;
    private final FollowRepository followRepository;

    @Transactional
    public Long createBadge(BadgeRequestDTO.BadgeCreateDto request) {
        // 중복 체크
        if (badgeRepository.findByBadgeName(request.getBadgeName()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 뱃지명입니다.");
        }

        // LOGIN_DAYS 타입은 conditionValue가 없어도 됨
        if (request.getBadgeType() != BadgeType.LOGIN_DAYS && request.getConditionValue() == null && 
            (request.getStartDate() == null || request.getEndDate() == null)) {
            throw new IllegalArgumentException("LOGIN_DAYS가 아닌 경우 conditionValue 또는 (startDate와 endDate) 중 하나는 필수입니다.");
        }

        // 개수 관련 뱃지(REVIEW_COUNT, COLLECTION_COUNT, FOLLOW_COUNT, LIKE_COUNT)는 기간이 없어도 됨
        // startDate와 endDate가 모두 null이면 null로 설정, 하나라도 있으면 설정
        LocalDateTime startDate = request.getStartDate();
        LocalDateTime endDate = request.getEndDate();
        
        // 개수 관련 뱃지는 기간이 없어도 되므로 자동으로 현재 시간을 설정하지 않음
        Badge badge = Badge.builder()
                .badgeName(request.getBadgeName())
                .badgeImage(request.getBadgeImage())
                .badgeType(request.getBadgeType())
                .badgeMission(request.getBadgeMission())
                .conditionValue(request.getConditionValue())
                .startDate(startDate)
                .endDate(endDate)
                .userBadges(List.of())
                .build();

        Badge savedBadge = badgeRepository.save(badge);
        return savedBadge.getBadgeId();
    }

    public BadgeResponseDTO.BadgeDetailDto getBadge(Long badgeId) {
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 뱃지입니다."));

        return BadgeResponseDTO.BadgeDetailDto.builder()
                .badgeId(badge.getBadgeId())
                .badgeName(badge.getBadgeName())
                .badgeImage(badge.getBadgeImage())
                .badgeType(badge.getBadgeType())
                .badgeMission(badge.getBadgeMission())
                .conditionValue(badge.getConditionValue())
                .startDate(badge.getStartDate())
                .endDate(badge.getEndDate())
                .build();
    }

    public List<BadgeResponseDTO.BadgeDetailDto> getAllBadges() {
        List<Badge> badges = badgeRepository.findAll();
        return badges.stream()
                .map(badge -> BadgeResponseDTO.BadgeDetailDto.builder()
                        .badgeId(badge.getBadgeId())
                        .badgeName(badge.getBadgeName())
                        .badgeImage(badge.getBadgeImage())
                        .badgeType(badge.getBadgeType())
                        .badgeMission(badge.getBadgeMission())
                        .conditionValue(badge.getConditionValue())
                        .startDate(badge.getStartDate())
                        .endDate(badge.getEndDate())
                        .build())
                .collect(Collectors.toList());
    }

    public List<BadgeResponseDTO.BadgeDetailDto> getActiveBadges() {
        List<Badge> badges = badgeRepository.findActiveBadges(LocalDateTime.now());
        return badges.stream()
                .map(badge -> BadgeResponseDTO.BadgeDetailDto.builder()
                        .badgeId(badge.getBadgeId())
                        .badgeName(badge.getBadgeName())
                        .badgeImage(badge.getBadgeImage())
                        .badgeType(badge.getBadgeType())
                        .badgeMission(badge.getBadgeMission())
                        .conditionValue(badge.getConditionValue())
                        .startDate(badge.getStartDate())
                        .endDate(badge.getEndDate())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateBadge(Long badgeId, BadgeRequestDTO.BadgeUpdateDto request) {
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 뱃지입니다."));

        if (request.getBadgeName() != null)
            badge.setBadgeName(request.getBadgeName());
        if (request.getBadgeImage() != null)
            badge.setBadgeImage(request.getBadgeImage());
        if (request.getBadgeType() != null)
            badge.setBadgeType(request.getBadgeType());
        if (request.getBadgeMission() != null)
            badge.setBadgeMission(request.getBadgeMission());
        if (request.getConditionValue() != null)
            badge.setConditionValue(request.getConditionValue());
        if (request.getStartDate() != null)
            badge.setStartDate(request.getStartDate());
        if (request.getEndDate() != null)
            badge.setEndDate(request.getEndDate());
    }

    @Transactional
    public void deleteBadge(Long badgeId) {
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 뱃지입니다."));
        
        // 뱃지 삭제 전에 관련된 모든 UserBadge를 먼저 삭제
        List<UserBadge> userBadges = userBadgeRepository.findByBadge(badge);
        if (!userBadges.isEmpty()) {
            log.info("뱃지 삭제 전 관련 UserBadge 삭제: badgeId={}, userBadgeCount={}", badgeId, userBadges.size());
            userBadgeRepository.deleteAll(userBadges);
        }
        
        badgeRepository.delete(badge);
        log.info("뱃지 삭제 완료: badgeId={}, badgeName={}", badgeId, badge.getBadgeName());
    }

    @Transactional
    public void assignBadgeToUser(Long userId, Long badgeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 뱃지입니다."));

        // 이미 보유한 뱃지인지 확인
        if (userBadgeRepository.existsByUserIdAndBadgeId(userId, badgeId)) {
            throw new IllegalArgumentException("이미 보유한 뱃지입니다.");
        }

        // 활성 뱃지인지 확인
        LocalDateTime now = LocalDateTime.now();
        if (badge.getStartDate() != null && badge.getStartDate().isAfter(now)) {
            throw new IllegalArgumentException("아직 시작되지 않은 뱃지입니다.");
        }
        if (badge.getEndDate() != null && badge.getEndDate().isBefore(now)) {
            throw new IllegalArgumentException("이미 종료된 뱃지입니다.");
        }

        UserBadgeId userBadgeId = new UserBadgeId(userId, badgeId);
        UserBadge userBadge = UserBadge.builder()
                .id(userBadgeId)
                .user(user)
                .badge(badge)
                .build();

        userBadgeRepository.save(userBadge);
    }

    /**
     * 자동 뱃지 부여용 내부 메서드 (예외를 던지지 않음)
     */
    @Transactional
    private void assignBadgeToUserInternal(Long userId, Long badgeId) {
        try {
            // 이미 보유한 뱃지인지 확인
            if (userBadgeRepository.existsByUserIdAndBadgeId(userId, badgeId)) {
                return; // 이미 보유한 경우 그냥 리턴
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
            Badge badge = badgeRepository.findById(badgeId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 뱃지입니다."));

            // 활성 뱃지인지 확인
            LocalDateTime now = LocalDateTime.now();
            if (badge.getStartDate() != null && badge.getStartDate().isAfter(now)) {
                return; // 아직 시작되지 않은 뱃지
            }
            if (badge.getEndDate() != null && badge.getEndDate().isBefore(now)) {
                return; // 이미 종료된 뱃지
            }

            UserBadgeId userBadgeId = new UserBadgeId(userId, badgeId);
            UserBadge userBadge = UserBadge.builder()
                    .id(userBadgeId)
                    .user(user)
                    .badge(badge)
                    .build();

            userBadgeRepository.save(userBadge);
        } catch (Exception e) {
            log.error("뱃지 자동 부여 중 오류 발생: userId={}, badgeId={}", userId, badgeId, e);
        }
    }

    @Transactional
    public void removeBadgeFromUser(Long userId, Long badgeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 뱃지입니다."));

        UserBadge userBadge = userBadgeRepository.findByUserAndBadge(user, badge)
                .orElseThrow(() -> new IllegalArgumentException("보유하지 않은 뱃지입니다."));

        userBadgeRepository.delete(userBadge);
    }

    public BadgeResponseDTO.UserBadgeListDto getUserBadges(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        List<UserBadge> userBadges = userBadgeRepository.findByUser(user);
        List<BadgeResponseDTO.UserBadgeDto> badgeDtos = userBadges.stream()
                .map(ub -> BadgeResponseDTO.UserBadgeDto.builder()
                        .userId(ub.getUser().getUserId())
                        .badgeId(ub.getBadge().getBadgeId())
                        .badgeName(ub.getBadge().getBadgeName())
                        .badgeImage(ub.getBadge().getBadgeImage())
                        .badgeType(ub.getBadge().getBadgeType())
                        .badgeMission(ub.getBadge().getBadgeMission())
                        .build())
                .collect(Collectors.toList());

        return BadgeResponseDTO.UserBadgeListDto.of(userId, badgeDtos);
    }

    /**
     * 리뷰 작성 후 자동 뱃지 체크
     * 리뷰 작성 개수에 따라 뱃지를 자동으로 부여합니다.
     */
    @Transactional
    public void checkAndAssignReviewCountBadge(Long userId) {
        try {
            // 사용자의 리뷰 개수 조회
            // TODO: 성능 최적화를 위해 ReviewRepository에 countByUser_UserId 메서드 추가 고려
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
            long reviewCount = user.getReviews() != null ? user.getReviews().size() : 0;

            // 활성화된 REVIEW_COUNT 타입 뱃지 조회
            LocalDateTime now = LocalDateTime.now();
            List<Badge> activeReviewBadges = badgeRepository.findActiveBadgesByType(BadgeType.REVIEW_COUNT, now);

            // 조건을 만족하는 뱃지 자동 부여
            for (Badge badge : activeReviewBadges) {
                if (badge.getConditionValue() == null) {
                    log.warn("뱃지 conditionValue가 null: badgeId={}", badge.getBadgeId());
                    continue; // conditionValue가 없으면 스킵
                }
                if (reviewCount >= badge.getConditionValue()) {
                    assignBadgeToUserInternal(userId, badge.getBadgeId());
                    log.info("뱃지 자동 부여: userId={}, badgeId={}, badgeName={}, reviewCount={}, conditionValue={}", 
                            userId, badge.getBadgeId(), badge.getBadgeName(), reviewCount, badge.getConditionValue());
                }
            }
        } catch (Exception e) {
            log.error("리뷰 개수 뱃지 체크 중 오류 발생: userId={}", userId, e);
        }
    }

    /**
     * 컬렉션 생성 후 자동 뱃지 체크
     * 컬렉션 생성 개수에 따라 뱃지를 자동으로 부여합니다.
     */
    @Transactional
    public void checkAndAssignCollectionCountBadge(Long userId) {
        try {
            // 사용자의 컬렉션 개수 조회
            long collectionCount = collectionRepository.findByUser_UserId(userId).size();

            // 활성화된 COLLECTION_COUNT 타입 뱃지 조회
            LocalDateTime now = LocalDateTime.now();
            List<Badge> activeCollectionBadges = badgeRepository.findActiveBadgesByType(BadgeType.COLLECTION_COUNT, now);

            // 조건을 만족하는 뱃지 자동 부여
            for (Badge badge : activeCollectionBadges) {
                if (badge.getConditionValue() == null) {
                    log.warn("뱃지 conditionValue가 null: badgeId={}", badge.getBadgeId());
                    continue; // conditionValue가 없으면 스킵
                }
                if (collectionCount >= badge.getConditionValue()) {
                    assignBadgeToUserInternal(userId, badge.getBadgeId());
                    log.info("뱃지 자동 부여: userId={}, badgeId={}, badgeName={}, collectionCount={}, conditionValue={}", 
                            userId, badge.getBadgeId(), badge.getBadgeName(), collectionCount, badge.getConditionValue());
                }
            }
        } catch (Exception e) {
            log.error("컬렉션 개수 뱃지 체크 중 오류 발생: userId={}", userId, e);
        }
    }

    /**
     * 팔로우 추가 후 자동 뱃지 체크
     * 팔로우 개수에 따라 뱃지를 자동으로 부여합니다.
     */
    @Transactional
    public void checkAndAssignFollowCountBadge(Long userId) {
        try {
            // 사용자의 팔로우 개수 조회 (follower 기준)
            long followCount = followRepository.findByFollower_UserId(userId).size();

            log.info("팔로우 개수 체크: userId={}, followCount={}", userId, followCount);

            // 활성화된 FOLLOW_COUNT 타입 뱃지 조회
            LocalDateTime now = LocalDateTime.now();
            List<Badge> activeFollowBadges = badgeRepository.findActiveBadgesByType(BadgeType.FOLLOW_COUNT, now);

            log.info("활성화된 FOLLOW_COUNT 뱃지 개수: {}", activeFollowBadges.size());

            // 조건을 만족하는 뱃지 자동 부여
            for (Badge badge : activeFollowBadges) {
                if (badge.getConditionValue() == null) {
                    log.warn("뱃지 conditionValue가 null: badgeId={}", badge.getBadgeId());
                    continue; // conditionValue가 없으면 스킵
                }
                
                log.info("뱃지 체크: badgeId={}, badgeName={}, conditionValue={}, followCount={}", 
                        badge.getBadgeId(), badge.getBadgeName(), badge.getConditionValue(), followCount);
                
                if (followCount >= badge.getConditionValue()) {
                    assignBadgeToUserInternal(userId, badge.getBadgeId());
                    log.info("뱃지 자동 부여 성공: userId={}, badgeId={}, badgeName={}, followCount={}, conditionValue={}", 
                            userId, badge.getBadgeId(), badge.getBadgeName(), followCount, badge.getConditionValue());
                } else {
                    log.info("뱃지 조건 미달: userId={}, badgeId={}, followCount={}, conditionValue={}", 
                            userId, badge.getBadgeId(), followCount, badge.getConditionValue());
                }
            }
        } catch (Exception e) {
            log.error("팔로우 개수 뱃지 체크 중 오류 발생: userId={}", userId, e);
        }
    }

    /**
     * 리뷰 좋아요 추가 후 자동 뱃지 체크
     * 리뷰 작성자가 받은 총 좋아요 개수에 따라 뱃지를 자동으로 부여합니다.
     */
    @Transactional
    public void checkAndAssignLikeCountBadge(Long reviewAuthorId) {
        try {
            // 리뷰 작성자가 받은 총 좋아요 개수를 직접 쿼리로 계산
            Long totalLikeCount = reviewRepository.countTotalLikesByUserId(reviewAuthorId);
            if (totalLikeCount == null) {
                totalLikeCount = 0L;
            }

            log.info("좋아요 개수 체크: userId={}, totalLikeCount={}", reviewAuthorId, totalLikeCount);

            // 활성화된 LIKE_COUNT 타입 뱃지 조회
            LocalDateTime now = LocalDateTime.now();
            List<Badge> activeLikeBadges = badgeRepository.findActiveBadgesByType(BadgeType.LIKE_COUNT, now);

            log.info("활성화된 LIKE_COUNT 뱃지 개수: {}", activeLikeBadges.size());

            // 조건을 만족하는 뱃지 자동 부여
            for (Badge badge : activeLikeBadges) {
                if (badge.getConditionValue() == null) {
                    log.warn("뱃지 conditionValue가 null: badgeId={}", badge.getBadgeId());
                    continue; // conditionValue가 없으면 스킵
                }
                
                log.info("뱃지 체크: badgeId={}, badgeName={}, conditionValue={}, totalLikeCount={}", 
                        badge.getBadgeId(), badge.getBadgeName(), badge.getConditionValue(), totalLikeCount);
                
                if (totalLikeCount >= badge.getConditionValue()) {
                    assignBadgeToUserInternal(reviewAuthorId, badge.getBadgeId());
                    log.info("뱃지 자동 부여 성공: userId={}, badgeId={}, badgeName={}, totalLikeCount={}, conditionValue={}", 
                            reviewAuthorId, badge.getBadgeId(), badge.getBadgeName(), totalLikeCount, badge.getConditionValue());
                } else {
                    log.info("뱃지 조건 미달: userId={}, badgeId={}, totalLikeCount={}, conditionValue={}", 
                            reviewAuthorId, badge.getBadgeId(), totalLikeCount, badge.getConditionValue());
                }
            }
        } catch (Exception e) {
            log.error("좋아요 개수 뱃지 체크 중 오류 발생: reviewAuthorId={}", reviewAuthorId, e);
        }
    }

    /**
     * 로그인 시 자동 뱃지 체크
     * startDate와 endDate 사이에 로그인하면 뱃지를 자동으로 부여합니다.
     */
    @Transactional
    public void checkAndAssignLoginDaysBadge(Long userId) {
        try {
            LocalDateTime now = LocalDateTime.now();
            
            // 활성화된 LOGIN_DAYS 타입 뱃지 조회
            List<Badge> activeLoginBadges = badgeRepository.findActiveBadgesByType(BadgeType.LOGIN_DAYS, now);

            // 조건을 만족하는 뱃지 자동 부여
            for (Badge badge : activeLoginBadges) {
                // startDate와 endDate 사이에 로그인했는지 확인
                LocalDateTime startDate = badge.getStartDate();
                LocalDateTime endDate = badge.getEndDate();
                
                if (startDate != null && endDate != null) {
                    if (now.isAfter(startDate) && now.isBefore(endDate)) {
                        assignBadgeToUserInternal(userId, badge.getBadgeId());
                        log.info("뱃지 자동 부여: userId={}, badgeId={}, badgeName={}", userId, badge.getBadgeId(), badge.getBadgeName());
                    }
                } else if (startDate != null && now.isAfter(startDate)) {
                    // endDate가 null이고 startDate 이후면 부여
                    assignBadgeToUserInternal(userId, badge.getBadgeId());
                    log.info("뱃지 자동 부여: userId={}, badgeId={}, badgeName={}", userId, badge.getBadgeId(), badge.getBadgeName());
                }
            }
        } catch (Exception e) {
            log.error("로그인 뱃지 체크 중 오류 발생: userId={}", userId, e);
        }
    }

    /**
     * 특정 타입의 뱃지를 체크하고 자동으로 부여하는 통합 메서드
     */
    @Transactional
    public void checkAndAssignBadgeByType(Long userId, BadgeType badgeType) {
        switch (badgeType) {
            case REVIEW_COUNT:
                checkAndAssignReviewCountBadge(userId);
                break;
            case COLLECTION_COUNT:
                checkAndAssignCollectionCountBadge(userId);
                break;
            case FOLLOW_COUNT:
                checkAndAssignFollowCountBadge(userId);
                break;
            case LIKE_COUNT:
                checkAndAssignLikeCountBadge(userId);
                break;
            case LOGIN_DAYS:
                checkAndAssignLoginDaysBadge(userId);
                break;
        }
    }
}

