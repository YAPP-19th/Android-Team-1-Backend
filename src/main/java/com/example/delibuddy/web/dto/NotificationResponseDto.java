package com.example.delibuddy.web.dto;

import com.example.delibuddy.domain.notification.Notification;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationResponseDto {

    private Long id;
    private String title;
    private String body;
    private String route;
    private Long commentId;
    private Long partyId;

    public NotificationResponseDto(Notification notification) {
        id = notification.getId();
        title = notification.getTitle();
        body = notification.getBody();
        route = notification.getRoute();
        commentId = notification.getCommentId();
        partyId = notification.getPartyId();
    }

}
