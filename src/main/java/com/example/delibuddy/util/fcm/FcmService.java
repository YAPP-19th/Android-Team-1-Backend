package com.example.delibuddy.util.fcm;

import com.google.firebase.messaging.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Service
public class FcmService {
    private static Logger logger = LoggerFactory.getLogger(FcmService.class);

    public static void sendToToken(FcmRequest fcmRequest, String token) {

        // See documentation on defining a message payload.
        Message message = Message.builder()
            .setNotification(
                Notification.builder().setTitle(fcmRequest.getTitle()).setBody(fcmRequest.getBody()).build()
            )
            .putData("route", fcmRequest.getRoute())
            .setToken(token)
            .build();

        // Send a message to the device corresponding to the provided
        // registration token.
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            logger.error(e.toString());
        }

    }
}
