package com.example.delibuddy.configs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class FcmConfig {

    @Value("${GOOGLE_APPLICATION_CREDENTIALS}")
    private String firebaseConfigPath;

    Logger logger = LoggerFactory.getLogger(FcmConfig.class);

    @PostConstruct
    public void init() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(
                GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
            ).build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
