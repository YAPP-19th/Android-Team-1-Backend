package com.example.delibuddy.web.dto;

import com.example.delibuddy.domain.comment.Comment;
import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.user.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CommentResponseDto {
    private Long id;
    private Comment parent;
    private List<Comment> children;
    private String body;
    private Party party;
    private User writer;

    public CommentResponseDto(Comment entity) {
        id = entity.getId();
        parent = entity.getParent();
        children = entity.getChildren();
        body = entity.getBody();
        party = entity.getParty();
        writer = entity.getWriter();
    }
}
