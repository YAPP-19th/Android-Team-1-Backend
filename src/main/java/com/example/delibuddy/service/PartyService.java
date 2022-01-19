package com.example.delibuddy.service;


import com.example.delibuddy.domain.ban.Ban;
import com.example.delibuddy.domain.ban.BanRepository;
import com.example.delibuddy.domain.party.*;
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
    private final PartyFactory partyFactory;

    @Transactional
    public PartyResponseDto create(String leaderKakaoId, PartyCreationRequestDto dto) {
        User leader = userRepository.findByKakaoId(leaderKakaoId).get();
        Party party = partyFactory.createParty(dto, leader);
        partyRepository.save(party);
        party.join(partyUserRepository.save(PartyUser.builder().user(leader).party(party).build()));
        return new PartyResponseDto(party, Boolean.TRUE); // 자신이 생성한 파티에는 항상 속해있기 때문에 TRUE
    }

    @Transactional(readOnly = true)
    public PartyResponseDto getParty(Long id, String userKakaoId) {
        User user = userRepository.findByKakaoId(userKakaoId).get();
        Party party = partyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 파티가 없습니다. id=" + id));
        return new PartyResponseDto(party, party.isIn(user));
    }

    @Transactional(readOnly = true)
    public List<PartyResponseDto> getPartiesInCircle(String point, int distance, String userKakaoId) {
        User user = userRepository.findByKakaoId(userKakaoId).get();
        return partyRepository.findPartiesNear(point, distance).stream()
                .map(party->new PartyResponseDto(party, party.isIn(user)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PartyResponseDto> getMyParties(String userKakaoId) {
        User user = userRepository.findByKakaoId(userKakaoId).get();
        return user.getParties().stream()
                .map(partyUser->new PartyResponseDto(partyUser.getParty(), Boolean.TRUE)) // 자신이 속한 파티를 호출하므로, 항상 TRUE
                .collect(Collectors.toList());
    }

    @Transactional
    public void ban(String leaderKakaoId, Long partyId, Long targetId) {
        Party party = partyRepository.getById(partyId);
        User leader = userRepository.findByKakaoId(leaderKakaoId).get();
        User target = userRepository.getById(targetId);

        if (!leader.getId().equals(party.getLeader().getId())) {
            throw new IllegalArgumentException("파티장만 강퇴할 수 있습니다.");
        }

        if (target.getId().equals(party.getLeader().getId())) {
            throw new IllegalArgumentException("파티장을 강퇴할 수는 없습니다.");
        }

        partyUserRepository.deleteByPartyIdAndUserId(partyId, target.getId());
        party.getUsers().removeIf(pu -> pu.getUser().getId().equals(target.getId()));
        // party user 가 삭제된 것을 party user repository 는 알지만
        // partyRepository 는 delete 된 party user 를 여전히 가리키고 있음 (find() 로 다시 가져와도...!) 실화임?
        // https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=rorean&logNo=221466846173
        // 이걸 수동으로 끊으라고 하네...? 와...
        banRepository.save(Ban.builder().party(party).user(target).build());
    }

    @Transactional
    public void join(Long partyId, String userKakaoId) {
        User user = userRepository.findByKakaoId(userKakaoId).get();

        if (banRepository.findByPartyIdAndUserId(partyId, user.getId()).isPresent()) {
            throw new IllegalArgumentException("강퇴당한 파티입니다.");
        }

        // 내역이 없다면 join party 가 join 가능 상태인지 체크. 아니라면 throw
        Party party = partyRepository.getById(partyId);

        if (party.isIn(user)) {
            throw new IllegalArgumentException("이미 가입한 파티입니다."); // 왜 다른 exception 은 던질 수 없는데 IllegalArgumentException 은 덜질 수 있지?
        }

        PartyUser partyUser = partyUserRepository.save(PartyUser.builder().user(user).party(party).build());
        party.join(partyUser);
    }

    @Transactional
    public void leave(Long partyId, String userKakaoId) {
        User user = userRepository.findByKakaoId(userKakaoId).get();
        Party party = partyRepository.getById(partyId);

        if (!party.isIn(user)) {
            throw new IllegalArgumentException("가입되어 있지 않은 파티입니다.");
        }

        if (party.getLeader().getId().equals(user.getId())) {
            throw new IllegalArgumentException("파티장은 탈퇴할 수 없습니다.");
        }

        party.leave(partyUserRepository.findByPartyIdAndUserId(party.getId(), user.getId()).get());
        partyUserRepository.deleteByPartyIdAndUserId(party.getId(), user.getId());
    }

    @Transactional
    public void edit(String leaderKakaoId, Long partyId, PartyEditRequestDto dto) {
        Party party = partyRepository.getById(partyId);
        User leader = userRepository.findByKakaoId(leaderKakaoId).get();

        if (!leader.getId().equals(party.getLeader().getId())) {
            throw new IllegalArgumentException("파티장만 파티를 수정할 수 있습니다.");
        }

        party.edit(dto);
    }

    @Transactional
    public void changeStatus(String leaderKakaoId, Long partyId, String status) {
        Party party = partyRepository.getById(partyId);
        User leader = userRepository.findByKakaoId(leaderKakaoId).get();

        if (!leader.getId().equals(party.getLeader().getId())) {
            throw new IllegalArgumentException("파티장만 파티를 수정할 수 있습니다.");
        }

        party.setStatus(PartyStatus.statusBy(status));
    }

    @Transactional
    public void delete(Long id) {
        Party party = partyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("party 가 없습니다."));

        // if (party.getId() ) // 여기서 유저가 party 장이 맞는지 체크해야함.
        partyRepository.delete(party);
    }

//    @Transactional(readOnly = true)
//    public List<PartyResponseDto> getPartiesInGeom(String wkt) {
//         초기 기획에서 제외되었습니다~
//        return partyRepository.findPartiesInGeom(wkt).stream()
//                .map(PartyResponseDto::new)
//                .collect(Collectors.toList());
//    }
}
