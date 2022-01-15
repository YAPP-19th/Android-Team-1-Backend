package com.example.delibuddy.service;

import com.example.delibuddy.domain.notification.Notification;
import com.example.delibuddy.domain.notification.NotificationRepository;
import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.domain.user.UserRepository;
import com.example.delibuddy.web.dto.CategoryResponseDto;
import com.example.delibuddy.web.dto.NotificationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getMyNotifications(String userKakaoId) {
        User me = userRepository.findByKakaoId(userKakaoId).get();

        return notificationRepository.findByUser(me).stream()
                .map(NotificationResponseDto::new)
                .collect(Collectors.toList());
    }

}
