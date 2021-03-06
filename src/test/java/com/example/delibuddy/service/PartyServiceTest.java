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
    void ????????????_????????????_?????????_???_??????() {
        // Given: ???????????? ?????? ????????? ??????.
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

        // Expect: ????????? ????????? ????????? ???????????? ?????? ?????????.
        IllegalArgumentException illegalArgumentException = assertThrows(
            IllegalArgumentException.class,
            () -> partyService.ban(other.getKakaoId(), party.getId(), you.getId())
        );
        System.out.println("illegalArgumentException = " + illegalArgumentException);
    }

    @Test
    void ????????????_???????????????() {
        // Given: ?????? ??? ???
        User me = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );

        Category category = categoryRepository.save(new Category("HiCategory", "Hi", "google.com", "FFFFFF"));

        // When: ?????? ??????
        PartyResponseDto dto = partyService.create(
            me.getKakaoId(),
            new PartyCreationRequestDto("my party", "body", "", "", "", "POINT (1 1)", category.getId(), 5, LocalDateTime.now())
        );

        // Then: ??? ????????????
        Party party = partyRepository.getById(dto.getId());
        assertThat(party).isNotNull();
        assertThat(party.isIn(me)).isTrue();
    }

    @Test
    void ????????????_?????????_???_??????() {
        // Given: party ??? user ??????, ????????? ??????. ????????? ??????
        User me = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );
        Party party = partyRepository.save(Party.builder().leader(me).build());

        // Expect: IllegalArgumentException ??? ?????????.
        assertThrows(IllegalArgumentException.class, () -> partyService.ban(me.getKakaoId(), party.getId(), me.getId()));
    }

    @Test
    void ????????????_?????????_???_??????() {
        // Given: party ??? user ??????, ????????? ??????. ????????? ??????
        Category category = categoryRepository.save(new Category("HiCategory", "Hi", "google.com", "FFFFFF"));

        User me = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );

        PartyResponseDto partyResponseDto = partyService.create(
                me.getKakaoId(),
                new PartyCreationRequestDto("my party", "body", "", "", "", "POINT (1 1)", category.getId(), 5, LocalDateTime.now())
        );

        // Expect: IllegalArgumentException ??? ?????????.
        assertThrows(IllegalArgumentException.class, () -> partyService.leave(partyResponseDto.getId(), me.getKakaoId()));
    }

    @Test
    void ??????_?????????_2???_?????????_???_??????() {
        // Given: ?????? ????????? ?????????
        Party party = createParty();
        User user = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );
        partyService.join(party.getId(), user.getKakaoId());

        // Expect: ????????? 2??? ???????????? ????????? ?????????.
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> partyService.join(party.getId(), user.getKakaoId()));
        System.out.println("illegalArgumentException = " + illegalArgumentException);
    }

    @Test
    void ?????????_?????????_??????????????????_?????????_???_??????() {

    }

    @Test
    void ?????????_??????_????????????_?????????_join_???_???_??????() {
        // Given: party ??? user ??????
        Party party1 = createParty();
        User user = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );

        // When: ????????? ??????
        partyService.join(party1.getId(), user.getKakaoId());

        // Then: party.users ?????? ??????, PartyUser ??? ???????????? ??????.
        Party resultParty = partyRepository.getById(party1.getId());
        assertThat(user).isIn(resultParty.getUsers().get(0).getUser());
        assertThat(partyUserRepository.findByPartyIdAndUserId(resultParty.getId(), user.getId()).isPresent()).isTrue();
    }

    @Test
    void ????????????_?????????_?????????_?????????_???_??????() {
        // Given: ???????????? ???
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

        // Expect: ????????? ?????? ????????? ?????? ?????? ????????? ?????????. ?????? ??????
        assertThrows(
                IllegalArgumentException.class,
                () -> partyService.edit(you.getKakaoId(), party.getId(), new PartyEditRequestDto("??????", "??????", "(2 2)"))
        );
    }

    @Test
    void ????????????_?????????_??????_???_???????????????() {
        // Given: party ??? user ??????, ????????? ??????. ????????? ??????
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

        // Expect: IllegalArgumentException ??? ?????????.
        // TODO: ????????? ????????? Exception ?????? ?????????
        assertThrows(IllegalArgumentException.class, () -> partyService.join(party.getId(), you.getKakaoId()));
    }

    // TODO: ?????? ????????? ?????????, ????????? ?????? ?????????, ????????? ????????? ?????? ????????? ????????? ?????? ??????

}
