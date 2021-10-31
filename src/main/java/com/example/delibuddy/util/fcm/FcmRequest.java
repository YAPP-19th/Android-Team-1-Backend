package com.example.delibuddy.util.fcm;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FcmRequest {
    private final String title;
    private final String message;
    private final String topic;
    private final String token;
}
