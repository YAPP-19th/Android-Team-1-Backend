package com.example.delibuddy.web;

import com.example.delibuddy.domain.category.Category;
import com.example.delibuddy.domain.category.CategoryRepository;
import com.example.delibuddy.domain.notification.Notification;
import com.example.delibuddy.domain.notification.NotificationRepository;
import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.domain.user.UserRepository;
import com.example.delibuddy.service.PartyService;
import com.example.delibuddy.web.dto.NotificationResponseDto;
import com.example.delibuddy.web.dto.PartyCreationRequestDto;
import com.example.delibuddy.web.dto.PartyResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@Transactional
class NotificationControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationController notificationController;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PartyService partyService;

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
    public void 받은_노티피케이션_목록을_조회할_수_있다() {
        // Given
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
        Notification notification = notificationRepository.save(
                new Notification(
                        user,
                        "test_title",
                        "test_body",
                        "delibuddy://",
                        null,
                        partyResponseDto.getId()
                )
        );

        // When
        List<NotificationResponseDto> notificationList = notificationController.getNotificationList();

        // Then
        assertThat(notification.getId())
                .isIn(
                        notificationList.stream().map(NotificationResponseDto::getId).collect(Collectors.toList())
                );
    }

}
