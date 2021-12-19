package com.example.delibuddy.util;

import com.example.delibuddy.domain.user.User;
import lombok.Data;

@Data
public class KakaoMyInfo {

    private String kakaoId;
    private String kakaoEmail;
    private String nickName;

    public KakaoMyInfo(String kakaoId, String kakaoEmail, String nickName) {
        this.kakaoId = kakaoId;
        this.kakaoEmail = kakaoEmail;
        this.nickName = nickName;
    }

    public User toUser(String profileImage) {
        return User.builder().kakaoId(kakaoId).email(kakaoEmail).profileImage(profileImage).nickName(nickName).build();
    }
}
