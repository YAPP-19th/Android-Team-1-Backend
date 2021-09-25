package com.example.delibuddy.web.dto;

import com.example.delibuddy.domain.party.Party;
import lombok.Data;

@Data
public class PartyCreationRequestDto {

    private final int distance;


    public Party toEntity() {
        return Party.builder().build();
    }
}
