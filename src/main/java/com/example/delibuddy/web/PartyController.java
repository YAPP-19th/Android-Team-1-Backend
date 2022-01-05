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

    @GetMapping("${api.v1}/parties/{id}") // url parameter
    public PartyResponseDto getParty(@PathVariable Long id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return partyService.getParty(id, userDetails.getUsername());
    }

    @GetMapping("${api.v1}/parties/circle")
    public List<PartyResponseDto> getPartiesInCircle(@RequestParam String point, @RequestParam int distance) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return partyService.getPartiesInCircle(point, distance, userDetails.getUsername());
    }

    @GetMapping("${api.v1}/parties/me")
    public List<PartyResponseDto> getMyParties() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return partyService.getMyParties(userDetails.getUsername());
    }

    @PutMapping("${api.v1}/parties/{id}")
    public OkayDto editParty(@RequestBody PartyEditRequestDto requestDto, @PathVariable Long id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        partyService.edit(userDetails.getUsername(), id, requestDto);
        return new OkayDto();
    }

    @PutMapping("${api.v1}/parties/{id}/status")
    public OkayDto changeStatus(@RequestBody PartyChangeStatusRequestDto requestDto, @PathVariable Long id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        partyService.changeStatus(userDetails.getUsername(), id, requestDto.getStatus());
        return new OkayDto();
    }

    @PostMapping("${api.v1}/parties/{id}/join")
    public OkayDto joinParty(@PathVariable Long id) {
        // 이것도 okay 만 보내면 될 듯?
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        partyService.join(id, userDetails.getUsername());
        return new OkayDto();
    }

    @PostMapping("${api.v1}/parties/{id}/leave")
    public OkayDto leaveParty(@PathVariable Long id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        partyService.leave(id, userDetails.getUsername());
        return new OkayDto();
    }

    @PostMapping("${api.v1}/parties/{id}/ban")
    public OkayDto banFromParty(@PathVariable Long id, @RequestBody PartyBanRequestDto partyBanRequestDto) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        partyService.ban(userDetails.getUsername(), id, partyBanRequestDto.getTargetId());
        return new OkayDto();
    }

    @GetMapping("${api.v1}/parties/geom")
    public OkayDto getPartiesInGeom() {
        return new OkayDto();
    }

    @DeleteMapping("${api.v1}/parties/{id}")
    public OkayDto deleteParty(@PathVariable Long id) {
        // 이것도 okay 만 보내면 될 듯?
        partyService.delete(id);
        return new OkayDto();
    }
}
