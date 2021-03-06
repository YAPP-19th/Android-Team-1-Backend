package com.example.delibuddy.web.dto;

import com.example.delibuddy.domain.party.Party;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import com.example.delibuddy.domain.party.PartyStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class PartyResponseDto {
    private final long id;
    private final String title;
    private final String body;
    private String placeName;
    private String placeNameDetail;
    private String openKakaoUrl;
    private UserResponseDto leader;
    private final String coordinate;
    private final CategoryResponseDto category;
    private final Integer targetUserCount;
    private final Integer currentUserCount;
    private final String status;
    private final List<UserResponseDto> users;
    private final Boolean isIn;



    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private final LocalDateTime orderTime; // TODO: db 에는 9시간 전으로 들어감... timezone 문제가 있는 듯... mysql connection string 에서 serverTimeZone UTC+9 같은걸 해야 하나?

    private final List<String> allStatuses = Arrays.stream(PartyStatus.values()).map(PartyStatus::getStatus).collect(Collectors.toList());

    public PartyResponseDto(Party entity, Boolean isIn) {
        id = entity.getId();
        title = entity.getTitle();
        body = entity.getBody();
        placeName = entity.getPlaceName();
        placeNameDetail = entity.getPlaceNameDetail();
        openKakaoUrl = entity.getOpenKakaoUrl();
        leader = new UserResponseDto(entity.getLeader());
        coordinate = "POINT (" + entity.getCoordinate().getX() + " " + entity.getCoordinate().getY() + ")";
        category = new CategoryResponseDto(entity.getCategory());
        currentUserCount = entity.getUsers().size();
        targetUserCount = entity.getTargetUserCount();
        orderTime = entity.getOrderTime();
        status = entity.getStatus().getStatus();
        users = entity.getUsers().stream().map(partyUSer->new UserResponseDto(partyUSer.getUser())).collect(Collectors.toList());
        this.isIn = isIn;
    }
}