package com.example.delibuddy.web.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class PartyBanRequestDto {
    private final Long targetId;
}
