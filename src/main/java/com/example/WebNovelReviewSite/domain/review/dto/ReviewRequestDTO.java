package com.example.WebNovelReviewSite.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

public class ReviewRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDto {
        private Long userId;
        private Long novelId;
        private String content;
        private BigDecimal star;
        private List<String> hashtags;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateDto {
        private String content;
        private BigDecimal star;
    }
}
