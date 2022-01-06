package com.example.delibuddy.web;

import com.example.delibuddy.domain.category.Category;
import com.example.delibuddy.domain.category.CategoryRepository;
import com.example.delibuddy.domain.comment.Comment;
import com.example.delibuddy.domain.comment.CommentRepository;
import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.party.PartyFactory;
import com.example.delibuddy.domain.party.PartyRepository;
import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.domain.user.UserRepository;
import com.example.delibuddy.service.MyUserDetailsService;
import com.example.delibuddy.web.dto.CommentCreationRequestDto;
import com.example.delibuddy.web.dto.PartyCreationRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Transactional
public class CommentControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PartyFactory partyFactory;

    private User user;

    private MockMvc mvc;

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
    public void 댓글이_등록된다() throws Exception {
        //given : 파티와 유저와 댓글 달 내용
        Category category = categoryRepository.save(new Category("hihi", "hi", "google.com", "FFFFFF"));
        Party party = partyRepository.save(
            partyFactory.createParty(
                new PartyCreationRequestDto("test", "test", "", "", "", "POINT (1 1)", category.getId(), 5, LocalDateTime.now()),
                this.user
            )
        );

        String body = "테스트 댓글";
        CommentCreationRequestDto requestDto = CommentCreationRequestDto.builder()
                .body(body)
                .partyId(party.getId())
                .build();

        String url = "http://localhost:8080/api/v1/comments";

        //when : POST 요청 보냄
        final ResultActions actions = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)));



        //then : 생성된 댓글이 리스폰스로 옴
//        actions
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("body").value(body))
//                .andExpect(jsonPath("party").value(party))
//                .andExpect(jsonPath("writer").value(user));

        List<Comment> all = commentRepository.findAll();
        assertThat(all.get(0).getBody()).isEqualTo(body);
        assertThat(all.get(0).getParty()).isEqualTo(party);
        assertThat(all.get(0).getWriter()).isEqualTo(user);
    }
}
