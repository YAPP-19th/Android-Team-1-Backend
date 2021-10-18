package com.example.delibuddy.service;


import com.example.delibuddy.domain.ban.Ban;
import com.example.delibuddy.domain.ban.BanRepository;
import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.party.PartyUser;
import com.example.delibuddy.domain.party.PartyUserRepository;
import com.example.delibuddy.domain.party.PartyRepository;
import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.domain.user.UserRepository;
import com.example.delibuddy.web.dto.PartyCreationRequestDto;
import com.example.delibuddy.web.dto.PartyEditRequestDto;
import com.example.delibuddy.web.dto.PartyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor // 생성자를 통해 bean 을 주입받는게 가장 이상적인 방법이라고 해서 사용했습니다.
@Service
public class PartyService {

    private final PartyRepository partyRepository;
    private final UserRepository userRepository;
    private final PartyUserRepository partyUserRepository;
    private final BanRepository banRepository;

    public void ban(String leaderKakaoId, Long partyId, String targetKakaoId) {
        Party party = partyRepository.getById(partyId);
        User leader = userRepository.findByKakaoId(leaderKakaoId).get();
        User target = userRepository.findByKakaoId(targetKakaoId).get();

        if (!leader.getId().equals(party.getLeader().getId())) {
            throw new IllegalArgumentException("파티장만 강퇴할 수 있습니다.");
        }

        if (target.getId().equals(party.getLeader().getId())) {
            throw new IllegalArgumentException("파티장을 강퇴할 수는 없습니다.");
        }

        partyUserRepository.deleteByPartyIdAndUserId(partyId, target.getId());
        banRepository.save(Ban.builder().party(party).user(target).build());
    }

    public void join(Long partyId, String userKakaoId) {
        User user = userRepository.findByKakaoId(userKakaoId).get();

        if (banRepository.findByPartyIdAndUserId(partyId, user.getId()).isPresent()) {
            throw new IllegalArgumentException("강퇴당한 파티입니다.");
        }

        // 내역이 없다면 join party 가 join 가능 상태인지 체크. 아니라면 throw
        Party party = partyRepository.getById(partyId);

        if (!party.isIn(user)) {
            throw new IllegalArgumentException("이미 가입한 파티입니다."); // 왜 다른 exception 은 던질 수 없는데 IllegalArgumentException 은 덜질 수 있지?
        }

        PartyUser partyUser = partyUserRepository.save(PartyUser.builder().user(user).party(party).build());
        party.join(partyUser);
    }

    public void edit(String leaderKakaoId, Long partyId, PartyEditRequestDto dto) {
        Party party = partyRepository.getById(partyId);
        User leader = userRepository.findByKakaoId(leaderKakaoId).get();

        if (!leader.getId().equals(party.getLeader().getId())) {
            throw new IllegalArgumentException("파티장만 파티를 수정할 수 있습니다.");
        }

        party.edit(dto);
    }

    @Transactional
    public PartyResponseDto create(String leaderKakaoId, PartyCreationRequestDto dto) {
        User leader = userRepository.findByKakaoId(leaderKakaoId).get();
        Party party = dto.toEntity();
        partyRepository.save(party);
        PartyUser partyUser = partyUserRepository.save(PartyUser.builder().user(leader).party(party).build());
        party.join(partyUser);
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

    @Transactional(readOnly = true)
    public List<PartyResponseDto> getPartiesInGeom(String wkt) {
        // TODO: 테스트 필요
        return partyRepository.findPartiesInGeom(wkt).stream()
                .map(PartyResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PartyResponseDto> getMyParties(String point, int distance) {
        // TODO: 구현하기
        return partyRepository.findPartiesNear(point, distance).stream()
                .map(PartyResponseDto::new)
                .collect(Collectors.toList());
    }

}
