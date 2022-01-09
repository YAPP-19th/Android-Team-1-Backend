package com.example.delibuddy.service;

import com.example.delibuddy.domain.notification.Notification;
import com.example.delibuddy.domain.notification.NotificationRepository;
import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.party.PartyRepository;
import com.example.delibuddy.domain.party.PartyUser;
import com.example.delibuddy.util.fcm.FcmRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.delibuddy.util.fcm.FcmUtil.sendToToken;


@RequiredArgsConstructor
@Service
public class FcmService {

    // TODO: test 할 떄 mocking 해서 FcmService.sendToToken 호출되었는지 검증하기

    private final PartyRepository partyRepository;
    private final NotificationRepository notificationRepository;

    public void newComment(Long partyId, Long commentId) {
        // 이로써 createComment 컨트롤러 안에서 party 쿼리가 두 번 일어나는데... 1차 캐시가 도움이 될 지 모르곘네효~
        Party party = partyRepository.getById(partyId);

        String title = "파티에 새로운 댓글이 달렸습니다.";
        String body = "딜리버디와 함께 새로운 댓글을 확인해 보아요 \uD83C\uDFB5~";
        String route = "delibuddy://comment?partyId=" + party.getId().toString() + "&commentId=" + commentId.toString();

        FcmRequest fcmRequest = FcmRequest.builder()
                .title(title)
                .body(body)
                .route(route)
                .build();

        for (PartyUser partyUser: party.getUsers()) {
            if (partyUser.getUser().getFcmToken() != null && !partyUser.getUser().getFcmToken().isEmpty()) {
                sendToToken(fcmRequest, party.getLeader().getFcmToken());
                notificationRepository.save(
                        Notification.builder()
                                .user(partyUser.getUser())
                                .title(title)
                                .body(body)
                                .route(route)
                                .partyId(partyId)
                                .commentId(commentId)
                                .build()
                );
            }
        }
    }

}
