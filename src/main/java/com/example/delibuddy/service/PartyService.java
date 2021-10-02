package com.example.delibuddy.service;


import com.example.delibuddy.domain.ban.BanRepository;
import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.party.PartyRepository;
import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.domain.user.UserRepository;
import com.example.delibuddy.web.auth.MyUserDetails;
import com.example.delibuddy.web.dto.PartyCreationRequestDto;
import com.example.delibuddy.web.dto.PartyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor // 생성자를 통해 bean 을 주입받는게 가장 이상적인 방법이라고 해서 사용했습니다.
@Service
public class PartyService {

    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final BanRepository banRepository;

    public void ban() {

    }

    public void join(Long id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByKakaoId(userDetails.getUsername()).get();
        // ban 내역이 있는지 먼저 조회. 있다면 throw
        System.out.println("user = " + user);
        // 내역이 없다면 join party 가 join 가능 상태인지 체크. 아니라면 throw

        // PartyMember 생성

    }

    public void edit() {

    }

    @Transactional
    public PartyResponseDto create(PartyCreationRequestDto dto) {
        Party party = dto.toEntity();
        partyRepository.save(party);
        return new PartyResponseDto(party);
    }

    @Transactional
    public void delete(Long id) {
        Party party = partyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("party 가 없습니다."));

        // if (party.getId() ) // 여기서 유저가 party 장이 맞는지 체크해야함.
        partyRepository.delete(party);
    }

    @Transactional(readOnly = true)
    public PartyResponseDto getParty(Long id) {
        return new PartyResponseDto(partyRepository.getById(id));
    }

    @Transactional(readOnly = true)
    public List<PartyResponseDto> getPartiesInCircle(String point, int distance) {
        return partyRepository.findPartiesNear(point, distance).stream()
                .map(PartyResponseDto::new)
                .collect(Collectors.toList());
    }

}
