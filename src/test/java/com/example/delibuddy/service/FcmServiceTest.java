package com.example.delibuddy.service;

import com.example.delibuddy.domain.category.Category;
import com.example.delibuddy.domain.category.CategoryRepository;
import com.example.delibuddy.domain.notification.Notification;
import com.example.delibuddy.domain.notification.NotificationRepository;
import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.domain.user.UserRepository;
import com.example.delibuddy.web.dto.CommentCreationRequestDto;
import com.example.delibuddy.web.dto.CommentResponseDto;
import com.example.delibuddy.web.dto.PartyCreationRequestDto;
import com.example.delibuddy.web.dto.PartyResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class FcmServiceTest {

    @Autowired
    FcmService fcmService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PartyService partyService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void party_에_새_댓글이_달리면_알림이_간다() {
        // Given
        Category category = categoryRepository.save(new Category("HiCategory", "Hi", "google.com", "FFFFFF"));

        User me = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );
        me.setFcmToken("ejCQjxUVRWGBtT_tk5gu4Z:APA91bGMEjAXlvVRrldgIX30CaQe1AOyEtPpvhLOM2kHKCw9n5PTTctNmues_wp-asUfN8FyQaaDCzuNN1SHKO_G6drIAOGAvgNz_51dYFYD4UvL8_VdeNVBc0DDQaJBKuvbMa86JvPh");

        PartyResponseDto partyResponseDto = partyService.create(
                me.getKakaoId(),
                new PartyCreationRequestDto("my party", "body", "", "", "", "POINT (1 1)", category.getId(), 5, LocalDateTime.now())
        );

        CommentResponseDto commentResponseDto = commentService.create(
                CommentCreationRequestDto.builder().partyId(partyResponseDto.getId()).body("test").build(), me.getKakaoId()
        );


        // When
        fcmService.newComment(partyResponseDto.getId(), commentResponseDto.getId());

        // Then
        List<Notification> notifications = notificationRepository.findByCommentId(commentResponseDto.getId());
        assertThat(notifications.stream().map(n -> n.getUser().getId()).collect(Collectors.toList())).contains(me.getId());
    }
}
