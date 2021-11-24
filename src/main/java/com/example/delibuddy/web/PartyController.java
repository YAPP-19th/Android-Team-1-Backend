package com.example.delibuddy.web;

import com.example.delibuddy.service.PartyService;
import com.example.delibuddy.web.auth.MyUserDetails;
import com.example.delibuddy.web.dto.*;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Party")
@RequiredArgsConstructor
@RestController
public class PartyController {

    private final PartyService partyService;

    @PostMapping("${api.v1}/parties")
    public PartyResponseDto createParty(@RequestBody PartyCreationRequestDto requestDto) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return partyService.create(userDetails.getUsername(), requestDto);
    }

    @PutMapping("${api.v1}/parties/{id}")
    public OkayDto editParty(@RequestBody PartyEditRequestDto requestDto, @PathVariable Long id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        partyService.edit(userDetails.getUsername(), id, requestDto);
        return new OkayDto();
    }

    @PutMapping("${api.v1}/parties/{id}/status")
    public OkayDto changeStatus(@RequestBody PartyChangeStatusRequestDto requestDto, @PathVariable Long id) {
        // todo 구현하기. 파티는 OPEN -> ORDERING -> DONE 상태로 proceed 합니다.
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new OkayDto();
    }

    @DeleteMapping("${api.v1}/parties/{id}")
    public OkayDto deleteParty(@RequestParam Long id) {
        // 이것도 okay 만 보내면 될 듯?
        partyService.delete(id);
        return new OkayDto();
    }

    @PostMapping("${api.v1}/parties/{id}/join")
    public PartyResponseDto joinParty(@PathVariable Long id) {
        // 이것도 okay 만 보내면 될 듯?
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        partyService.join(id, userDetails.getUsername());
        return new PartyResponseDto(1L, "", "");
    }

    @PostMapping("${api.v1}/parties/{id}/leave")
    public PartyResponseDto leaveParty(@RequestParam Long id) {
        // 이것도 okay 만 보내면 될 듯?
        return new PartyResponseDto(1L, "", "");
    }

    @PostMapping("${api.v1}/parties/{id}/ban")
    public PartyResponseDto banFromParty(@RequestParam Long id, @RequestBody PartyBanRequestDto partyBanRequestDto) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        partyService.ban(userDetails.getUsername(), id, partyBanRequestDto.getTargetId());
        return new PartyResponseDto(1L, "", "");
    }

    @GetMapping("${api.v1}/parties/{id}") // url parameter
    public PartyResponseDto getParty(@PathVariable Long id) {
        return partyService.getParty(id);
    }

    @GetMapping("${api.v1}/parties/circle")
    public List<PartyResponseDto> getPartiesInCircle(@RequestParam String point, @RequestParam int distance, @RequestParam String categories) {
        // todo: category 의 리스트도 조건에 포함되도록, query dsl 로 구현해보자.
        return partyService.getPartiesInCircle(point, distance);
    }

    @GetMapping("${api.v1}/parties/geom")
    public <List> PartyResponseDto[] getPartiesInGeom() {
        return new PartyResponseDto[] {new PartyResponseDto(1L, "", "")};
    }
}
