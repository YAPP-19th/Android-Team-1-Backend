package com.example.delibuddy.web.dto;

import com.example.delibuddy.domain.comment.Comment;
import com.example.delibuddy.domain.user.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class CommentUserResponseDto {
    private final String nickName;
    private final String profileImage;

    public CommentUserResponseDto(User entity) {
        nickName = entity.getNickName();
        profileImage = entity.getProfileImage();
    }
}
