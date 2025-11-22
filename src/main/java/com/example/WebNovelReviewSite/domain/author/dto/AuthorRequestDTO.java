package com.example.WebNovelReviewSite.domain.author.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthorRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDto {
        private Long userId;
        private String penName;
        private String nationality;
        private String debutYear;
        private String brief;
        private String profileImage;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateDto {
        private String penName;
        private String nationality;
        private String debutYear;
        private String brief;
        private String profileImage;
    }
}
