package com.example.delibuddy.web.dto;

import com.example.delibuddy.domain.comment.Comment;
import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class CommentResponseDto {
    private final Long id;
    private final Long parentId;
    private final List<CommentResponseDto> children;
    private final String body;
    private final Long partyId;
    private final CommentUserResponseDto writer;
    private final LocalDateTime createdAt;

    public CommentResponseDto(Comment entity) {
        id = entity.getId();
        parentId = entity.getParent() == null ? null : entity.getParent().getId();
        children = entity.getChildren().stream().map(CommentResponseDto::new).collect(Collectors.toList());
        body = entity.getIsDeleted()? new String("삭제된 댓글입니다.") : entity.getBody();
        partyId = entity.getParty().getId();
        writer = entity.getIsDeleted()? null : new CommentUserResponseDto(entity.getWriter());
        createdAt = entity.getCreatedAt();
    }
}
