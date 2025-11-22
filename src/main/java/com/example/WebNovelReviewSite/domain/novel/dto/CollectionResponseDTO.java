package com.example.WebNovelReviewSite.domain.novel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CollectionResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CollectionDetailDto {
        private Long collectionId;
        private Long userId;
        private String userName;
        private String collectionName;
        private String content;
        private Integer novelCount;
    }
}
