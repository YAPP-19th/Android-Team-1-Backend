package com.example.delibuddy.web.dto;

import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
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

    @Builder
    public PartyCreationRequestDto(String title, String body, String coordinate) {
        this.title = title;
        this.body = body;
        this.coordinate = coordinate;
    }
}
