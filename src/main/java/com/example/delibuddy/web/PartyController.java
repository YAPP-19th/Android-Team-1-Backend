package com.example.delibuddy.web;

import com.example.delibuddy.service.PartyService;
import com.example.delibuddy.web.dto.OkayDto;
import com.example.delibuddy.web.dto.PartyResponseDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Party")
@RequiredArgsConstructor
@RestController
public class PartyController {

    private final PartyService partyService;

    @PostMapping("${api.v1}/parties")
    public PartyResponseDto createParty() {
        return new PartyResponseDto(1L, "", "");
    }

    @PutMapping("${api.v1}/parties/{id}")
    public PartyResponseDto editParty(@RequestParam Long id) {
        return new PartyResponseDto(1L, "", "");
    }

    @DeleteMapping("${api.v1}/parties/{id}")
    public OkayDto deleteParty(@RequestParam Long id) {
        // 이것도 okay 만 보내면 될 듯?
        partyService.delete(id);
        return new OkayDto();
    }

    @PostMapping("${api.v1}/parties/{id}/join")
    public PartyResponseDto joinParty(@RequestParam Long id) {
        // 이것도 okay 만 보내면 될 듯?
        return new PartyResponseDto(1L, "", "");
    }

    @PostMapping("${api.v1}/parties/{id}/leave")
    public PartyResponseDto leaveParty(@RequestParam Long id) {
        // 이것도 okay 만 보내면 될 듯?
        return new PartyResponseDto(1L, "", "");
    }

    @PostMapping("${api.v1}/parties/{id}/ban")
    public PartyResponseDto banFromParty(@RequestParam Long id) {
        // 이거 그냥 okay 로 쏘면 되는데... dto 를 같이 써야 하는 문제가 발생하네...
        return new PartyResponseDto(1L, "", "");
    }

    @GetMapping("${api.v1}/parties/{id}") // url parameter
    public PartyResponseDto getParty(@RequestParam Long id) {
        return partyService.getParty(id);
    }

    @GetMapping("${api.v1}/parties/circle")
    public List<PartyResponseDto> getPartiesInCircle(@RequestParam String point, @RequestParam int distance, @RequestParam String categories) {
        return partyService.getPartiesInCircle(point, distance);
    }

    @GetMapping("${api.v1}/parties/geom")
    public <List> PartyResponseDto[] getPartiesInGeom() {
        return new PartyResponseDto[] {new PartyResponseDto(1L, "", "")};
    }
}
