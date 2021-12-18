package com.example.delibuddy.web.dto;

import com.example.delibuddy.domain.user.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserResponseDto {
    private final Long id;
    private final String nickName;
    private final String profileImage;
    private final Integer partiesCnt;

    public UserResponseDto(User entity) {
        id = entity.getId();
        nickName = entity.getNickName();
        profileImage = entity.getProfileImage();
        partiesCnt = entity.getParties().size();
    }

}
