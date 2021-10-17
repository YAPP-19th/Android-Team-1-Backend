package com.example.delibuddy.service;

import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.party.PartyRepository;
import com.example.delibuddy.domain.party.PartyUser;
import com.example.delibuddy.domain.party.PartyUserRepository;
import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.domain.user.UserRepository;
import com.example.delibuddy.web.dto.PartyResponseDto;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static com.example.delibuddy.util.GeoHelper.wktToGeometry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class PartyServiceTest {

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private PartyUserRepository partyUserRepository;

    @Autowired
    private PartyService partyService;

    @Autowired
    private UserRepository userRepository;

    private Party createParty() {
        Point point;
        try {
            point = (Point) wktToGeometry("POINT (1 1)");
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        Party party = Party.builder()
                .coordinate(point)
                .title("test title")
                .body("")
                .build();
        partyRepository.save(party);
        return party;
    }

    @Test
    void 파티를_id로_조회할_수_있다() {
        // Given: party 2개 생성
        Party party1 = createParty();
        Party party2 = createParty();

        // When: party service 로 쿼리
        PartyResponseDto partyDto = partyService.getParty(party1.getId());

        // Then: party1 의 Dto 를 얻는다.
        assertThat(new PartyResponseDto(party1)).isEqualTo(partyDto);
    }

    @Test
    void 파티장은_강퇴를_할_수_있다() {

    }

    @Test
    void 같은_파티에_2번_들어갈_수_없다() {

    }

    @Test
    void 파티의_상태가_주문완료라면_들어갈_수_없다() {

    }

    @Test
    void 파티에_이미_들어있지_않다면_join_할_수_있다() {

    }

    @Test
    void 파티장은_파티를_수정할_수_있다() {

    }

    @Test
    void 파티장이_아니면_파티를_수정할_수_없다() {

    }

    @Test
    void join_작동_테스트() {
        // Given: party 와 user 생성
        Party party1 = createParty();
        User user = userRepository.save(
            User.builder()
                .nickName("test")
                .kakaoId("test-kakao-id")
                .build()
        );

        // When: 파티에 입장
        partyService.join(party1.getId(), user.getKakaoId());

        // Then: party.users 안에 포함, PartyUser 도 존재해야 한다.
        Party resultParty = partyRepository.getById(party1.getId());
        assertThat(user).isIn(resultParty.getUsers().get(0).getUser());
        assertThat(partyUserRepository.findByPartyIdAndUserId(resultParty.getId(), user.getId()).isPresent()).isTrue();
    }

    @Test
    void 강퇴당한_사람은_다시_못_들어오지롱() {
        // Given: party 와 user 생성, 파티에 입장
        Party party1 = createParty();
        User user = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );
        partyService.join(party1.getId(), user.getKakaoId());
        partyService.ban(party1.getId(), user.getKakaoId());

        // Expect: IllegalArgumentException 이 터진다.
        // TODO: 나중에 적절한 Exception 으로 바꾸장
        assertThrows(IllegalArgumentException.class, () -> partyService.join(party1.getId(), user.getKakaoId()));
    }
}
