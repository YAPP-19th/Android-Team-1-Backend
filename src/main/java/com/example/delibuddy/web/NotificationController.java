package com.example.delibuddy.web;

import com.example.delibuddy.service.NotificationService;
import com.example.delibuddy.web.auth.MyUserDetails;
import com.example.delibuddy.web.dto.NotificationResponseDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "Notification")
@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("${api.v1}/notifications")
    public List<NotificationResponseDto> getNotificationList() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return notificationService.getMyNotifications(userDetails.getUsername());
    }

}
