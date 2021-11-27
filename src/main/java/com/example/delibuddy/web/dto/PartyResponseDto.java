package com.example.delibuddy.web.dto;

import com.example.delibuddy.domain.party.Party;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class PartyResponseDto {
    private final long id;
    private final String title;
    private final String body;
    private final String coordinate;
    private final CategoryResponseDto category;
    private final Integer targetUserCount;

    public PartyResponseDto(Party entity) {
        id = entity.getId();
        title = entity.getTitle();
        body = entity.getBody();
        coordinate = "POINT (" + entity.getCoordinate().getX() + " " + entity.getCoordinate().getY() + ")"; // TODO: coordinate 에 예시 넣기
        category = new CategoryResponseDto(entity.getCategory());
        targetUserCount = entity.getTargetUserCount();
    }
}
