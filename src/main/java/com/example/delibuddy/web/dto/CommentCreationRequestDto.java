package com.example.delibuddy.web.dto;

import com.example.delibuddy.domain.comment.Comment;
import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Builder
@Data
@RequiredArgsConstructor
public class CommentCreationRequestDto {
    private final String body;
    private final Long partyId;
    private final Long parentId;

    public Comment toEntity(User writer, Party party) {
        return Comment.builder()
                .body(body)
                .party(party)
                .writer(writer)
                .build();
    }

}