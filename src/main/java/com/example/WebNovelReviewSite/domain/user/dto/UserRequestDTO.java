package com.example.WebNovelReviewSite.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupDto {
        private String name;
        private String id;
        private String passwd;
        private String email;
        private String nickname;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDto {
        private String id;
        private String passwd;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateDto {
        private String name;
        private String email;
        private String passwd;
        private String nickname;
    }
}
