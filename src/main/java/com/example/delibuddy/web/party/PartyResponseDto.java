package com.example.delibuddy.web.party;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartyResponseDto {
    private final long id;
    private final String title;
    private final String body;
}
