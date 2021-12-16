package com.example.delibuddy.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartyCreationRequestDto {
    private String title;
    private String body;
    private String placeName;
    private String placeNameDetail;
    private String openKakaoUrl;
    private String coordinate;
    private Long categoryId;
    private Integer targetUserCount;

    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime orderTime;

}
