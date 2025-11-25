package com.example.WebNovelReviewSite.domain.badge.service;

import com.example.WebNovelReviewSite.domain.badge.dto.BadgeRequestDTO;
import com.example.WebNovelReviewSite.domain.badge.dto.BadgeResponseDTO;
import com.example.WebNovelReviewSite.domain.badge.entity.Badge;
import com.example.WebNovelReviewSite.domain.badge.entity.UserBadge;
import com.example.WebNovelReviewSite.domain.badge.entity.UserBadgeId;
import com.example.WebNovelReviewSite.domain.badge.repository.BadgeRepository;
import com.example.WebNovelReviewSite.domain.badge.repository.UserBadgeRepository;
import com.example.WebNovelReviewSite.domain.user.entity.User;
import com.example.WebNovelReviewSite.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createBadge(BadgeRequestDTO.BadgeCreateDto request) {
        // 중복 체크
        if (badgeRepository.findByBadgeName(request.getBadgeName()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 뱃지명입니다.");
        }

        Badge badge = Badge.builder()
                .badgeName(request.getBadgeName())
                .badgeImage(request.getBadgeImage())
                .badgeType(request.getBadgeType())
                .badgeMission(request.getBadgeMission())
                .conditionValue(request.getConditionValue())
                .startDate(request.getStartDate() != null ? request.getStartDate() : LocalDateTime.now())
                .endDate(request.getEndDate())
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
        badgeRepository.delete(badge);
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
}

