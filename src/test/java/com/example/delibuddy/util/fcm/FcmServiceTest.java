package com.example.delibuddy.util.fcm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FcmServiceTest {

    @Autowired
    FcmService fcmService;

    @Test
    public void fcm이_전송되어야_한다() {
        fcmService.sendMessageToToken(new FcmRequest("제목", "메시지", "", ""));
    }
}
