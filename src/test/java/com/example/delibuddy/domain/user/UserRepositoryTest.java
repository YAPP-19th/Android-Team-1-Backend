package com.example.delibuddy.domain.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void BaseTimeEntity_등록() { // TODO DB 연결도 안했는데 왜 테스트가 성공하지...?
        // Given
        LocalDateTime now = LocalDateTime.of(2019,6,4,0,0,0);
        userRepository.save(
                User.builder()
                .nickName("테스트 닉네임")
                .kakaoId("테스트-카카오-id")
                .build()
        );

        // When
        List<User> userList = userRepository.findAll();

        // Then
        User user = userList.get(0);
        System.out.println("user.nickName = " + user.getNickName());
        System.out.println("user.kakaoId = " + user.getKakaoId());
        System.out.println("user.ModifiedAt = " + user.getModifiedAt());
        System.out.println("user.CreatedAt = " + user.getCreatedAt());
    }
}