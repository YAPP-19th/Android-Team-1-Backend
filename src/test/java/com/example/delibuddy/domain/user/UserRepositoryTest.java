package com.example.delibuddy.domain.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void user_등록() {
        // Given
        String nickName = "테스트 닉네임";
        String kakaoId = "테스트-카카오-id";
        User user = userRepository.save(
                User.builder()
                        .nickName(nickName)
                        .kakaoId(kakaoId)
                        .build()
        );

        // When
        User resultUser =  userRepository.getById(user.getId());

        // Then
        assertEquals(nickName, resultUser.getNickName());
        assertEquals(kakaoId, resultUser.getKakaoId());
    }

    @Test
    public void kakao_id_는_unique_해야_한다() {

    }

}