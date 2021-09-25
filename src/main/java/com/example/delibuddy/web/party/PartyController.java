package com.example.delibuddy.web.party;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Party")
@RestController
public class PartyController {

    @GetMapping("${api.v1}/parties")
    public <List> PartyResponseDto[] getPartyList() {
        return new PartyResponseDto[] {new PartyResponseDto(1L, "", "")};
    }
}
