package com.example.delibuddy.web.dto;

import lombok.Getter;

@Getter
public class AuthenticationResponseDto {

    private final String jwt;
    private final Long userId;

    public AuthenticationResponseDto(String jwt, Long userId) {
        this.jwt = jwt;
        this.userId = userId;
    }
}
