package com.example.delibuddy.web;

import com.example.delibuddy.domain.ban.BanRepository;
import com.example.delibuddy.domain.category.Category;
import com.example.delibuddy.domain.category.CategoryRepository;
import com.example.delibuddy.domain.party.*;
import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.domain.user.UserRepository;
import com.example.delibuddy.service.MyUserDetailsService;
import com.example.delibuddy.service.PartyService;
import com.example.delibuddy.web.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.delibuddy.testhelper.PartyTestHelper.createPartyWithPointString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
    private PartyRepository partyRepository;

    @Autowired
    private PartyService partyService;

    @Autowired
    private PartyUserRepository partyUserRepository;

    @Autowired
    private BanRepository banRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PartyController partyController;

    @Autowired
    private PartyFactory partyFactory;

    @Autowired
    private WebApplicationContext context;

    private User user;

    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        user = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getKakaoId());
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, "", new ArrayList<>())
        );

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void createParty() throws Exception {
        // Given: 타이틀, 카테고리
        String title = "title";
        Category category = categoryRepository.save(new Category("hihi", "hi", "google.com", "FFFFFF"));

        // When: 파티 생성!
        MvcResult result = mvc.perform(
                post("http://localhost:8080/api/v1/parties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        new PartyCreationRequestDto(
                                                title,
                                                "body",
                                                "",
                                                "",
                                                "",
                                                "POINT (1 1)",
                                                category.getId(),
                                                5,
                                                LocalDateTime.now()
                                        )
                                )
                        )
        ).andReturn();

        // Then: 파티 생성 성공!
        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        Party party = partyRepository.getById(jsonNode.get("id").asLong());
        assertThat(party.getTitle()).isEqualTo("title");
        assertThat(party.getLeader().getId()).isEqualTo(user.getId());
    }

    @Test
    void editParty() {
        // Given: 파티
        Party party = partyRepository.save(Party.builder().leader(user).build());
        String title = "안녕?";

        // When: 파티 수정 요청을 보낸다
        partyController.editParty(new PartyEditRequestDto(title, "파티", "POINT (2 2)"), party.getId());

        // Then: 파티 제목이 수정되었다!
        assertThat(partyRepository.getById(party.getId()).getTitle()).isEqualTo(title);
    }

    @Test
    void changeStatus() {
        // Given
        Party party = partyRepository.save(Party.builder().leader(user).build());
        String status = PartyStatus.ORDERING.getStatus();

        // When: status 변경
        partyController.changeStatus(new PartyChangeStatusRequestDto(status), party.getId());

        // Then:
        party = partyRepository.getById(party.getId());
        assertThat(party.getStatus().getStatus()).isEqualTo(status);
    }

    @Test
    void deleteParty() throws Exception {
        // Given: 파티 하나 주어짐
        Category category = categoryRepository.save(new Category("hihi", "hi", "google.com", "FFFFFF"));
        partyService.create(
                user.getKakaoId(),
                new PartyCreationRequestDto(
                        "title",
                        "body",
                        "",
                        "",
                        "",
                        "POINT (1 1)",
                        category.getId(),
                        5,
                        LocalDateTime.now()
                )
        );
        Party party = partyRepository.save(Party.builder().leader(user).build());

        // When: 파티 삭제!
        MvcResult result = mvc.perform(
                delete(
                        "http://localhost:8080/api/v1/parties/" + party.getId().toString()
                ).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        // Then: 데이터베이스에서 삭제됩니다.
        // 컨트롤러 레밸의 테스트에서 repository 를 써서 데이터베이스를 확인하는게 과연 맞나?
        // 음.. 안될 게 뭐람?
        assertThat(partyRepository.findById(party.getId()).isPresent()).isFalse();
    }

    @Test
    void joinParty() {
        // Given: 유저2와 그의 파티
        User user2 = userRepository.save(
                User.builder()
                        .nickName("test2")
                        .kakaoId("test-kakao-id2")
                        .build()
        );
        Party party = partyRepository.save(Party.builder().leader(user2).build());

        // When: user 가 파티에 조인
        OkayDto okayDto = partyController.joinParty(party.getId());

        // Then: 파티의 Users 에 포함!
        assertThat(
            partyRepository.getById(party.getId())
            .getUsers().stream().map(PartyUser::getUser).collect(Collectors.toList())
        ).contains(user);
        assertThat(okayDto.isOkay()).isTrue();
    }

    @Test
    void leaveParty() {
        // Given: 유저2와 그의 파티
        User user2 = userRepository.save(
                User.builder()
                        .nickName("test2")
                        .kakaoId("test-kakao-id2")
                        .build()
        );
        Party party = partyRepository.save(Party.builder().leader(user2).build());
        PartyUser partyUser = partyUserRepository.save(new PartyUser(user, party));
        party.join(partyUser);

        // When: 파티를 떠난다.
        OkayDto okayDto = partyController.leaveParty(party.getId());

        // Then: 파티의 Users 에서 제거 되었다.
        assertThat(
            partyRepository.getById(party.getId())
            .getUsers().stream().map(PartyUser::getUser).collect(Collectors.toList())
        ).doesNotContain(user);
        assertThat(okayDto.isOkay()).isTrue();
    }

    @Test
    void banFromParty() {
        // Given: 유저1과 그의 파티
        User user2 = userRepository.save(
                User.builder()
                        .nickName("test2")
                        .kakaoId("test-kakao-id2")
                        .build()
        );
        Party party = partyRepository.save(Party.builder().leader(user).build());
        PartyUser partyUser = partyUserRepository.save(new PartyUser(user2, party));
        party.join(partyUser);

        // When: 강티~
        OkayDto okayDto = partyController.banFromParty(party.getId(), new PartyBanRequestDto(user2.getId()));

        // Then: 파티에서 퇴출 완료! Ban 기록이 남습니다.
        assertThat(banRepository.findByPartyIdAndUserId(party.getId(), user2.getId()).isPresent()).isTrue();
        assertThat(okayDto.isOkay()).isTrue();
    }

    @Test
    void getParty() {
        // Given: 선릉에 파티 둘
        Category category = categoryRepository.save(new Category("hihi", "hi", "google.com", "FFFFFF"));
        String 선릉_point = "POINT (127.048995 37.504506)";

        String title = "test1";
        String body = "test1";
        String placeName = "주소";
        String placeNameDetail = "상세주소";
        String openKakaoUrl = "https://open.kakao.com";

        Party 선릉1 = partyFactory.createParty(
                new PartyCreationRequestDto(title, body, placeName, placeNameDetail, openKakaoUrl, 선릉_point, category.getId(), 5, LocalDateTime.now()),
                this.user
        );
        Party 선릉2 = partyFactory.createParty(
                new PartyCreationRequestDto("test2", "test2", "", "", "", 선릉_point, category.getId(), 5, LocalDateTime.now()),
                this.user
        );
        partyRepository.save(선릉1);
        partyRepository.save(선릉2);

        // When: 하나만 아이디로 조횐
        PartyResponseDto party = partyController.getParty(선릉1.getId());

        // Then: 조회 성공
        assertThat(party.getId()).isEqualTo(선릉1.getId());
        assertThat(party.getTitle()).isEqualTo(title);
        assertThat(party.getBody()).isEqualTo(body);
        assertThat(party.getPlaceName()).isEqualTo(placeName);
        assertThat(party.getPlaceNameDetail()).isEqualTo(placeNameDetail);
        assertThat(party.getOpenKakaoUrl()).isEqualTo(openKakaoUrl);
    }

    @Test
    void getPartiesInCircle() {
        // Given: 선릉에 파티 하나
        Category category = categoryRepository.save(new Category("hihi", "hi", "google.com", "FFFFFF"));
        String 선릉_point = "POINT (127.048995 37.504506)";
        Party 선릉 = partyRepository.save(
            partyFactory.createParty(
                new PartyCreationRequestDto("test", "test", "", "", "", 선릉_point, category.getId(), 5, LocalDateTime.now()),
                this.user
            )
        );

        // When: 검색
        List<PartyResponseDto> partiesInCircle = partyController.getPartiesInCircle(선릉_point, 2000);

        // Then: 검색 결과에 반환
        assertThat(partiesInCircle.stream().map(PartyResponseDto::getId)).contains(선릉.getId());
    }

    @Test
    void getPartiesInGeom() {
        // 2021-11-06 지도 탭이 송두리째 날아갔습니다 ㅎㅎ
    }
}
