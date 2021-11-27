package com.example.delibuddy.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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

    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime orderTime;
}
