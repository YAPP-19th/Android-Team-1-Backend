package com.example.delibuddy.util.fcm;

import com.example.delibuddy.configs.FcmConfig;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FcmServiceTest {

    @Autowired
    private FcmConfig fcmConfig;

    @Test
    public void fcm_전송() {
        // This registration token comes from the client FCM SDKs.
        String registrationToken = "ejCQjxUVRWGBtT_tk5gu4Z:APA91bGMEjAXlvVRrldgIX30CaQe1AOyEtPpvhLOM2kHKCw9n5PTTctNmues_wp-asUfN8FyQaaDCzuNN1SHKO_G6drIAOGAvgNz_51dYFYD4UvL8_VdeNVBc0DDQaJBKuvbMa86JvPh";

        FcmService.sendToToken(
                FcmRequest.builder().title("제목").body("딜리버디와 함께 새로운 댓글을 확인해 보아요 \uD83C\uDFB5~").route("delibuddy://comment?partyId=87&commentId=36").build(),
                registrationToken
        );
    }
}
