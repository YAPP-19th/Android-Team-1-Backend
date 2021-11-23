package com.example.delibuddy.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartyCreationRequestDto {
    private String title;
    private String body;
    private String coordinate;
    private Long categoryId;
    private Integer targetUserCount;
    private LocalDateTime orderTime;
}
