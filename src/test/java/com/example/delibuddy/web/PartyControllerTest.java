package com.example.delibuddy.web;

import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.domain.user.UserRepository;
import com.example.delibuddy.service.MyUserDetailsService;
import com.example.delibuddy.web.dto.PartyCreationRequestDto;
import com.example.delibuddy.web.dto.PartyResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spi.service.contexts.SecurityContext;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class PartyControllerTest {
    // controller 의 일은 대부분 userDetails 을 가져와서 service 를 호출하기만 합니다. 이걸 과연 테스트 해야 할까? 라는 고민이 있었습니다.
    // 그래서...
    // controller 테스트는 스모크 & E2E 테스트로 정의하며, 요청이 성공하는 경우만 생각합니다. (그래서 스모크 테스트 인거죠)
    // e.g. ban from party 가 성공한다.

    // 디테일한 behavior 테스트는 repository test 에서 작성합니다.
    // e.g. 파티장을 ban 할 수 없다. 파티장이 아닌 사람은 ban 할 수 없다 등등

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PartyController partyController;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getKakaoId());
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, "", new ArrayList<>())
        );
    }

    @Test
    void createParty() {
        // Given: 타이틀
        String title = "title";

        // When: 파티 생성!
        PartyResponseDto party = partyController.createParty(
            new PartyCreationRequestDto(title, "body", "(1 1)")
        );

        // Then: 파티 생성 성공!
        assertThat(party.getTitle()).isEqualTo("title");
    }

    @Test
    void editParty() {
    }

    @Test
    void changeStatus() {
    }

    @Test
    void deleteParty() {
    }

    @Test
    void joinParty() {
    }

    @Test
    void leaveParty() {
    }

    @Test
    void banFromParty() {
    }

    @Test
    void getParty() {
    }

    @Test
    void getPartiesInCircle() {
    }

    @Test
    void getPartiesInGeom() {
    }
}