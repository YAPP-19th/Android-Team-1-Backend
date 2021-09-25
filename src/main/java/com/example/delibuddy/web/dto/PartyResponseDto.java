package com.example.delibuddy.web.dto;

import com.example.delibuddy.domain.party.Party;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor // TODO 없애기
public class PartyResponseDto {
    private final long id;
    private final String title;
    private final String body;

    public PartyResponseDto(Party entity) {
        id = entity.getId();
        title = entity.getTitle();
        body = entity.getBody();
    }
}
