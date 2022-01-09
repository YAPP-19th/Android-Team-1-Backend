package com.example.delibuddy.service;

import com.example.delibuddy.domain.notification.Notification;
import com.example.delibuddy.domain.notification.NotificationRepository;
import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.party.PartyRepository;
import com.example.delibuddy.domain.party.PartyUser;
import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.domain.user.UserRepository;
import com.example.delibuddy.util.fcm.FcmRequest;
import com.example.delibuddy.util.fcm.FcmUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class FcmService {

    // TODO: test 할 떄 mocking 해서 FcmService.sendToToken 호출되었는지 검증하기

    private final PartyRepository partyRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final FcmUtil fcmUtil;

    public void partyEdit(Long partyId) {
        Party party = partyRepository.getById(partyId);

        String title = "파티내용이 변경되었습니다.";
        String body = "";
        String route = "delibuddy://comment?partyId=" + party.getId().toString();

        FcmRequest fcmRequest = FcmRequest.builder()
                .title(title)
                .body(body)
                .route(route)
                .build();

        for (PartyUser partyUser: party.getUsers()) {
            if (partyUser.getUser().getFcmToken() != null && !partyUser.getUser().getFcmToken().isEmpty()) {
                fcmUtil.sendToToken(fcmRequest, party.getLeader().getFcmToken());
                notificationRepository.save(
                        Notification.builder()
                                .user(partyUser.getUser())
                                .title(title)
                                .body(body)
                                .route(route)
                                .partyId(partyId)
                                .build()
                );
            }
        }
    }

    public void partyStatusChanged(Long partyId, String partyStatus) {
        Party party = partyRepository.getById(partyId);

        String title = "파티의 상태가 " + partyStatus + "로 변경되었습니다.";
        String body = "";
        String route = "delibuddy://comment?partyId=" + party.getId().toString();

        FcmRequest fcmRequest = FcmRequest.builder()
                .title(title)
                .body(body)
                .route(route)
                .build();

        for (PartyUser partyUser: party.getUsers()) {
            if (partyUser.getUser().getFcmToken() != null && !partyUser.getUser().getFcmToken().isEmpty()) {
                fcmUtil.sendToToken(fcmRequest, party.getLeader().getFcmToken());
                notificationRepository.save(
                        Notification.builder()
                                .user(partyUser.getUser())
                                .title(title)
                                .body(body)
                                .route(route)
                                .partyId(partyId)
                                .build()
                );
            }
        }
    }

    public void partyJoin(Long partyId, String userKakaoId) {
        Party party = partyRepository.getById(partyId);
        User user = userRepository.findByKakaoId(userKakaoId).get();

        String title = "파티에 " + user.getNickName() + " 님이 참가하셨습니다!";
        String body = "가쥬아~";
        String route = "delibuddy://comment?partyId=" + party.getId().toString();

        FcmRequest fcmRequest = FcmRequest.builder()
                .title(title)
                .body(body)
                .route(route)
                .build();

        for (PartyUser partyUser: party.getUsers()) {
            if (partyUser.getUser().getFcmToken() != null && !partyUser.getUser().getFcmToken().isEmpty()) {
                fcmUtil.sendToToken(fcmRequest, party.getLeader().getFcmToken());
                notificationRepository.save(
                        Notification.builder()
                                .user(partyUser.getUser())
                                .title(title)
                                .body(body)
                                .route(route)
                                .partyId(partyId)
                                .build()
                );
            }
        }
    }

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
                fcmUtil.sendToToken(fcmRequest, party.getLeader().getFcmToken());
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
