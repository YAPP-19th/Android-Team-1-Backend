package com.example.delibuddy.testhelper;

import com.example.delibuddy.util.fcm.FcmRequest;
import com.example.delibuddy.util.fcm.FcmUtil;

public class FcmUtilMock implements FcmUtil {
    @Override
    public void sendToToken(FcmRequest fcmRequest, String token) {
        System.out.println("fcm 보내는 척~");
    }
}
