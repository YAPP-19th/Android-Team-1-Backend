package com.example.delibuddy.util.fcm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FcmServiceTest {

    @Autowired
    FcmService fcmService;
//    android client 가 준비가 되면 fcm_token 을 발급해서 테스트 해봅시다.
//    @Test
//    public void fcm 이_전송되어야_한다() {
//        fcmService.sendMessageToToken(
//            new FcmRequest("제목", "메시지", null, "eRT3h1u7RDmbPWAIjPaMxT:APA91bFPAJCy3ho70BDliJT4WUXyiN9caEP57Ls5edMtnTY9D2ZIM-VE1GDQDpmj_Wrt-6EYJtK4Gd4b5N94Wiiqu8-o7y3BR13hqXv6Xo1_ziCc1iRbf9hiSTexd-YMY-TrjpgZ3y6k")
//        );
//    }
}
