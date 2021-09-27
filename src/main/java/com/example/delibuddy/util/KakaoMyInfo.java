package com.example.delibuddy.util;

import com.example.delibuddy.domain.user.User;
import lombok.Data;

@Data
public class KakaoMyInfo {

    private String kakaoId;
    private String kakaoEmail;
    private String profileImage;
    private String nickName;

    public KakaoMyInfo(String kakaoId, String kakaoEmail, String profileImage, String nickName) {
        this.kakaoId = kakaoId;
        this.kakaoEmail = kakaoEmail;
        this.profileImage = profileImage;
        this.nickName = nickName;
    }

    public User toUser() {
        return User.builder().kakaoId(kakaoId).email(kakaoEmail).profileImage(profileImage).nickName(nickName).build();
    }
}
