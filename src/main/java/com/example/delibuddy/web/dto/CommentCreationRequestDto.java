package com.example.delibuddy.web.dto;

import com.example.delibuddy.domain.comment.Comment;
import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentCreationRequestDto {
    private String body;
    private Long partyId;
    private Long parentId;

    @Builder
    public CommentCreationRequestDto(String body, Long partyId, Long parentId) {
        this.body = body;
        this.partyId = partyId;
        this.parentId = parentId;
    }

    public Comment toEntity(User writer, Party party) {
        return Comment.builder()
                .body(body)
                .party(party)
                .writer(writer)
                .build();
    }

}
