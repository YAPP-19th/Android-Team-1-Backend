package com.example.delibuddy.web;

import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.domain.user.UserRepository;
import com.example.delibuddy.service.MyUserDetailsService;
import com.example.delibuddy.web.dto.FcmTokenRequest;
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

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@Transactional
class UserControllerTest {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

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
    void fcm_token_을_set_할_수_있다() throws Exception {

        String url = "http://localhost:8080/api/v1/users/me/fcm_token";
        String dummyFcmToken = "abc";

        FcmTokenRequest requestDto = new FcmTokenRequest(dummyFcmToken);

        //when : POST 요청 보냄
        final ResultActions actions = mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)));

        // Then
        User resultUser = userRepository.findByKakaoId(this.user.getKakaoId()).get();
        assertThat(resultUser.getFcmToken()).isEqualTo(dummyFcmToken);
    }
}
