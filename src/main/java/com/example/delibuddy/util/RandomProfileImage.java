package com.example.delibuddy.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomProfileImage {

    @Value("${s3.random_profile_image_url}")
    private String S3_BASE_URL;

    private String randomProfileImages[] = {
            "icon_user_profile_blue.png",
            "icon_user_profile_brown.png",
            "icon_user_profile_deep_blue.png",
            "icon_user_profile_green.png",
            "icon_user_profile_grey.png",
            "icon_user_profile_light_green.png",
            "icon_user_profile_pink.png",
            "icon_user_profile_purple.png",
            "icon_user_profile_red.png",
            "icon_user_profile_yellow.png"
        };

    private Random rand = new Random();

    public String getRandomProfileImage(){
        return S3_BASE_URL + randomProfileImages[rand.nextInt(randomProfileImages.length)];
    }
}
