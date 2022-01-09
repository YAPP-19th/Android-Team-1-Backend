package com.example.delibuddy.util.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class FcmUtiImpl implements FcmUtil{
    private static Logger logger = LoggerFactory.getLogger(FcmUtiImpl.class);

    public void sendToToken(FcmRequest fcmRequest, String token) {

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
