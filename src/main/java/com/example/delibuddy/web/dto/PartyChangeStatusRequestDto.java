package com.example.delibuddy.web.dto;

import com.example.delibuddy.domain.party.PartyStatus;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class PartyChangeStatusRequestDto {

    private String status;

    public PartyStatus getPartyStatus() {
        // todo: PartyStatus 를 찾아서 리턴하자.
        return PartyStatus.OPEN;
    }

}
