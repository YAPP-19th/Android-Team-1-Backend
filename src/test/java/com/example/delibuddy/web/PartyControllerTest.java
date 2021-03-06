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
    // controller ??? ?????? ????????? userDetails ??? ???????????? service ??? ??????????????? ?????????. ?????? ?????? ????????? ?????? ??????? ?????? ????????? ???????????????.
    // ?????????...
    // controller ???????????? ????????? & E2E ???????????? ????????????, ????????? ???????????? ????????? ???????????????. (????????? ????????? ????????? ?????????)
    // e.g. ban from party ??? ????????????.

    // ???????????? behavior ???????????? repository test ?????? ???????????????.
    // e.g. ???????????? ban ??? ??? ??????. ???????????? ?????? ????????? ban ??? ??? ?????? ??????

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
        // Given: ?????????, ????????????
        String title = "title";
        Category category = categoryRepository.save(new Category("hihi", "hi", "google.com", "FFFFFF"));

        // When: ?????? ??????!
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

        // Then: ?????? ?????? ??????!
        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        Party party = partyRepository.getById(jsonNode.get("id").asLong());
        assertThat(party.getTitle()).isEqualTo("title");
        assertThat(party.getLeader().getId()).isEqualTo(user.getId());
    }

    @Test
    void editParty() {
        // Given: ??????
        Party party = partyRepository.save(Party.builder().leader(user).build());
        String title = "???????";

        // When: ?????? ?????? ????????? ?????????
        partyController.editParty(new PartyEditRequestDto(title, "??????", "POINT (2 2)"), party.getId());

        // Then: ?????? ????????? ???????????????!
        assertThat(partyRepository.getById(party.getId()).getTitle()).isEqualTo(title);
    }

    @Test
    void changeStatus() {
        // Given
        Party party = partyRepository.save(Party.builder().leader(user).build());
        String status = PartyStatus.ORDERING.getStatus();

        // When: status ??????
        partyController.changeStatus(new PartyChangeStatusRequestDto(status), party.getId());

        // Then:
        party = partyRepository.getById(party.getId());
        assertThat(party.getStatus().getStatus()).isEqualTo(status);
    }

    @Test
    void deleteParty() throws Exception {
        // TODO: ???????????? ?????? CASCADE ??? ????????? ???????????? ???????????? ????????? ??????... ?????? ????????????
        // TODO: ?????? API ???????????? CASCADE ??? ??? ?????? ????????? ??????????????? ???????????? ???????????????.

        // Given: ?????? ?????? ?????????
        Category category = categoryRepository.save(new Category("hihi", "hi", "google.com", "FFFFFF"));
        PartyResponseDto partyResponseDto = partyService.create(
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
        assertThat(partyRepository.findById(partyResponseDto.getId()).isPresent()).isTrue();

        // When: ?????? ??????!
        MvcResult result = mvc.perform(
                delete(
                        "http://localhost:8080/api/v1/parties/" + partyResponseDto.getId()
                ).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        // Then: ???????????????????????? ???????????????.
        assertThat(partyRepository.findById(partyResponseDto.getId()).isPresent()).isFalse();
    }

    @Test
    void joinParty() {
        // Given: ??????2??? ?????? ??????
        User user2 = userRepository.save(
                User.builder()
                        .nickName("test2")
                        .kakaoId("test-kakao-id2")
                        .build()
        );
        Party party = partyRepository.save(Party.builder().leader(user2).build());

        // When: user ??? ????????? ??????
        OkayDto okayDto = partyController.joinParty(party.getId());

        // Then: ????????? Users ??? ??????!
        assertThat(
            partyRepository.getById(party.getId())
            .getUsers().stream().map(PartyUser::getUser).collect(Collectors.toList())
        ).contains(user);
        assertThat(okayDto.isOkay()).isTrue();
    }

    @Test
    void leaveParty() {
        // Given: ??????2??? ?????? ??????
        User user2 = userRepository.save(
                User.builder()
                        .nickName("test2")
                        .kakaoId("test-kakao-id2")
                        .build()
        );
        Party party = partyRepository.save(Party.builder().leader(user2).build());
        PartyUser partyUser = partyUserRepository.save(new PartyUser(user, party));
        party.join(partyUser);

        // When: ????????? ?????????.
        OkayDto okayDto = partyController.leaveParty(party.getId());

        // Then: ????????? Users ?????? ?????? ?????????.
        assertThat(
            partyRepository.getById(party.getId())
            .getUsers().stream().map(PartyUser::getUser).collect(Collectors.toList())
        ).doesNotContain(user);
        assertThat(okayDto.isOkay()).isTrue();
    }

    @Test
    void banFromParty() {
        // Given: ??????1??? ?????? ??????
        User user2 = userRepository.save(
                User.builder()
                        .nickName("test2")
                        .kakaoId("test-kakao-id2")
                        .build()
        );
        Party party = partyRepository.save(Party.builder().leader(user).build());
        PartyUser partyUser = partyUserRepository.save(new PartyUser(user2, party));
        party.join(partyUser);

        // When: ??????~
        OkayDto okayDto = partyController.banFromParty(party.getId(), new PartyBanRequestDto(user2.getId()));

        // Then: ???????????? ?????? ??????! Ban ????????? ????????????.
        assertThat(banRepository.findByPartyIdAndUserId(party.getId(), user2.getId()).isPresent()).isTrue();
        assertThat(okayDto.isOkay()).isTrue();
    }

    @Test
    void getParty() {
        // Given: ????????? ?????? ???
        Category category = categoryRepository.save(new Category("hihi", "hi", "google.com", "FFFFFF"));
        String ??????_point = "POINT (127.048995 37.504506)";

        String title = "test1";
        String body = "test1";
        String placeName = "??????";
        String placeNameDetail = "????????????";
        String openKakaoUrl = "https://open.kakao.com";

        Party ??????1 = partyFactory.createParty(
                new PartyCreationRequestDto(title, body, placeName, placeNameDetail, openKakaoUrl, ??????_point, category.getId(), 5, LocalDateTime.now()),
                this.user
        );
        Party ??????2 = partyFactory.createParty(
                new PartyCreationRequestDto("test2", "test2", "", "", "", ??????_point, category.getId(), 5, LocalDateTime.now()),
                this.user
        );
        partyRepository.save(??????1);
        partyRepository.save(??????2);

        // When: ????????? ???????????? ??????
        PartyResponseDto party = partyController.getParty(??????1.getId());

        // Then: ?????? ??????
        assertThat(party.getId()).isEqualTo(??????1.getId());
        assertThat(party.getTitle()).isEqualTo(title);
        assertThat(party.getBody()).isEqualTo(body);
        assertThat(party.getPlaceName()).isEqualTo(placeName);
        assertThat(party.getPlaceNameDetail()).isEqualTo(placeNameDetail);
        assertThat(party.getOpenKakaoUrl()).isEqualTo(openKakaoUrl);
    }

    @Test
    void getPartiesInCircle() {
        // Given: ????????? ?????? ??????
        Category category = categoryRepository.save(new Category("hihi", "hi", "google.com", "FFFFFF"));
        String ??????_point = "POINT (127.048995 37.504506)";
        Party ?????? = partyRepository.save(
            partyFactory.createParty(
                new PartyCreationRequestDto("test", "test", "", "", "", ??????_point, category.getId(), 5, LocalDateTime.now()),
                this.user
            )
        );

        // When: ??????
        List<PartyResponseDto> partiesInCircle = partyController.getPartiesInCircle(??????_point, 2000);

        // Then: ?????? ????????? ??????
        assertThat(partiesInCircle.stream().map(PartyResponseDto::getId)).contains(??????.getId());
    }

    @Test
    void getPartiesInGeom() {
        // 2021-11-06 ?????? ?????? ???????????? ?????????????????? ??????
    }
}
