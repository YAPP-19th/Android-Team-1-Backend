package com.example.delibuddy.service;

import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.domain.user.UserRepository;
import com.example.delibuddy.web.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponseDto getMyInfo(String myKakaoId) {
        User user = userRepository.findByKakaoId(myKakaoId).get();
        return new UserResponseDto(user);
    }

    @Transactional
    public void setFcmToken(String myKakaoId, String fcmToken) {
        User user = userRepository.findByKakaoId(myKakaoId).get();
        user.setFcmToken(fcmToken);
    }
}
