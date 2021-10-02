package com.example.delibuddy.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticationRequestDto {

    private String token;

    public AuthenticationRequestDto(String token) {
        this.token = token;
    }
}
