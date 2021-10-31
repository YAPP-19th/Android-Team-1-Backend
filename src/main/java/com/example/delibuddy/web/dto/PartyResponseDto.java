package com.example.delibuddy.web.dto;

import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.user.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
        // TODO: 세상에... Party 가 User 대신 필요도 없는 PartyUser 를 가져온다... User 를 가져오려면 어떻게 해야됨??
    }
}
