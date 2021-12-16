package com.example.delibuddy.service;

import com.example.delibuddy.domain.ban.BanRepository;
import com.example.delibuddy.domain.category.Category;
import com.example.delibuddy.domain.category.CategoryRepository;
import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.party.PartyRepository;
import com.example.delibuddy.domain.party.PartyUser;
import com.example.delibuddy.domain.party.PartyUserRepository;
import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.domain.user.UserRepository;
import com.example.delibuddy.web.dto.PartyCreationRequestDto;
import com.example.delibuddy.web.dto.PartyEditRequestDto;
import com.example.delibuddy.web.dto.PartyResponseDto;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

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

    @Autowired
    private CategoryRepository categoryRepository;

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
    void 파티장이_아니라면_강퇴할_수_없다() {
        // Given: 파티장과 너와 이상한 사람.
        User me = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );

        User you = userRepository.save(
                User.builder()
                        .nickName("test2")
                        .kakaoId("test-kakao-id2")
                        .build()
        );

        User other = userRepository.save(
                User.builder()
                        .nickName("test3")
                        .kakaoId("test-kakao-id3")
                        .build()
        );
        Party party = partyRepository.save(Party.builder().leader(me).build());
        partyService.join(party.getId(), you.getKakaoId());

        // Expect: 이상한 사람이 강퇴를 시도하면 예외 터진다.
        IllegalArgumentException illegalArgumentException = assertThrows(
            IllegalArgumentException.class,
            () -> partyService.ban(other.getKakaoId(), party.getId(), you.getId())
        );
        System.out.println("illegalArgumentException = " + illegalArgumentException);
    }

    @Test
    void 파티장도_파티멤버다() {
        // Given: 사람 한 명
        User me = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );

        Category category = categoryRepository.save(new Category("HiCategory", "Hi", "google.com", "FFFFFF"));

        // When: 파티 생성
        PartyResponseDto dto = partyService.create(
            me.getKakaoId(),
            new PartyCreationRequestDto("my party", "body", "", "", "", "POINT (1 1)", category.getId(), 5, LocalDateTime.now())
        );

        // Then: 잘 생성된당
        Party party = partyRepository.getById(dto.getId());
        assertThat(party).isNotNull();
        assertThat(party.isIn(me)).isTrue();
    }

    @Test
    void 파티장은_강퇴할_수_없다() {
        // Given: party 와 user 생성, 파티에 입장. 그리고 강퇴
        User me = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );
        Party party = partyRepository.save(Party.builder().leader(me).build());

        // Expect: IllegalArgumentException 이 터진다.
        assertThrows(IllegalArgumentException.class, () -> partyService.ban(me.getKakaoId(), party.getId(), me.getId()));
    }

    @Test
    void 같은_파티에_2번_들어갈_수_없다() {
        // Given: 이미 파티에 들어감
        Party party = createParty();
        User user = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );
        partyService.join(party.getId(), user.getKakaoId());

        // Expect: 파티에 2번 입장하면 예외가 터진다.
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> partyService.join(party.getId(), user.getKakaoId()));
        System.out.println("illegalArgumentException = " + illegalArgumentException);
    }

    @Test
    void 파티의_상태가_주문완료라면_들어갈_수_없다() {

    }

    @Test
    void 파티에_이미_들어있지_않다면_join_할_수_있다() {
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
    void 파티장이_아니면_파티를_수정할_수_없다() {
        // Given: 파티장과 너
        User me = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );

        User you = userRepository.save(
                User.builder()
                        .nickName("test2")
                        .kakaoId("test-kakao-id2")
                        .build()
        );
        Party party = partyRepository.save(Party.builder().leader(me).build());
        partyService.join(party.getId(), you.getKakaoId());

        // Expect: 리더가 아닌 사람이 파티 수정 요청을 보낸다. 예외 터짐
        assertThrows(
                IllegalArgumentException.class,
                () -> partyService.edit(you.getKakaoId(), party.getId(), new PartyEditRequestDto("안녕", "파티", "(2 2)"))
        );
    }

    @Test
    void 강퇴당한_사람은_다시_못_들어오지롱() {
        // Given: party 와 user 생성, 파티에 입장. 그리고 강퇴
        User me = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );
        User you = userRepository.save(
                User.builder()
                        .nickName("test2")
                        .kakaoId("test-kakao-id2")
                        .build()
        );
        Party party = partyRepository.save(Party.builder().leader(me).build());
        partyService.join(party.getId(), you.getKakaoId());
        partyService.ban(me.getKakaoId(), party.getId(), you.getId());

        // Expect: IllegalArgumentException 이 터진다.
        // TODO: 나중에 적절한 Exception 으로 바꾸장
        assertThrows(IllegalArgumentException.class, () -> partyService.join(party.getId(), you.getKakaoId()));
    }

}
