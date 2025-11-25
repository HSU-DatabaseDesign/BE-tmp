package com.example.WebNovelReviewSite.domain.badge.controller;

import com.example.WebNovelReviewSite.domain.badge.dto.BadgeRequestDTO;
import com.example.WebNovelReviewSite.domain.badge.dto.BadgeResponseDTO;
import com.example.WebNovelReviewSite.domain.badge.service.BadgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "뱃지 관리", description = "뱃지 생성, 조회, 수정, 삭제 및 유저 뱃지 할당 관련 API")
@RestController
@RequestMapping("/badges")
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    @Operation(summary = "뱃지 생성", description = "새로운 뱃지를 생성합니다.")
    @PostMapping
    public ResponseEntity<Long> createBadge(@Valid @RequestBody BadgeRequestDTO.BadgeCreateDto request) {
        Long badgeId = badgeService.createBadge(request);
        return ResponseEntity.ok(badgeId);
    }

    @Operation(summary = "뱃지 조회", description = "뱃지 ID로 뱃지 정보를 조회합니다.")
    @GetMapping("/{badgeId}")
    public ResponseEntity<BadgeResponseDTO.BadgeDetailDto> getBadge(@PathVariable Long badgeId) {
        BadgeResponseDTO.BadgeDetailDto badge = badgeService.getBadge(badgeId);
        return ResponseEntity.ok(badge);
    }

    @Operation(summary = "전체 뱃지 조회", description = "모든 뱃지 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<BadgeResponseDTO.BadgeDetailDto>> getAllBadges() {
        List<BadgeResponseDTO.BadgeDetailDto> badges = badgeService.getAllBadges();
        return ResponseEntity.ok(badges);
    }

    @Operation(summary = "활성 뱃지 조회", description = "현재 활성화된 뱃지 목록을 조회합니다.")
    @GetMapping("/active")
    public ResponseEntity<List<BadgeResponseDTO.BadgeDetailDto>> getActiveBadges() {
        List<BadgeResponseDTO.BadgeDetailDto> badges = badgeService.getActiveBadges();
        return ResponseEntity.ok(badges);
    }

    @Operation(summary = "뱃지 수정", description = "뱃지 정보를 수정합니다.")
    @PutMapping("/{badgeId}")
    public ResponseEntity<Void> updateBadge(@PathVariable Long badgeId,
                                             @Valid @RequestBody BadgeRequestDTO.BadgeUpdateDto request) {
        badgeService.updateBadge(badgeId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "뱃지 삭제", description = "뱃지를 삭제합니다.")
    @DeleteMapping("/{badgeId}")
    public ResponseEntity<Void> deleteBadge(@PathVariable Long badgeId) {
        badgeService.deleteBadge(badgeId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "유저에게 뱃지 할당", description = "특정 유저에게 뱃지를 할당합니다.")
    @PostMapping("/assign")
    public ResponseEntity<Void> assignBadgeToUser(@Valid @RequestBody BadgeRequestDTO.UserBadgeAssignDto request) {
        badgeService.assignBadgeToUser(request.getUserId(), request.getBadgeId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "유저 뱃지 제거", description = "유저의 뱃지를 제거합니다.")
    @DeleteMapping("/users/{userId}/badges/{badgeId}")
    public ResponseEntity<Void> removeBadgeFromUser(@PathVariable Long userId, @PathVariable Long badgeId) {
        badgeService.removeBadgeFromUser(userId, badgeId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "유저 뱃지 목록 조회", description = "특정 유저가 보유한 모든 뱃지를 조회합니다.")
    @GetMapping("/users/{userId}")
    public ResponseEntity<BadgeResponseDTO.UserBadgeListDto> getUserBadges(@PathVariable Long userId) {
        BadgeResponseDTO.UserBadgeListDto userBadges = badgeService.getUserBadges(userId);
        return ResponseEntity.ok(userBadges);
    }
}

