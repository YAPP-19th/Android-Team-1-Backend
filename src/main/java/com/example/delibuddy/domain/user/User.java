package com.example.delibuddy.domain.user;

import com.example.delibuddy.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String nickName;

    @Column(length = 100, nullable = false, unique = true)
    private String kakaoId;

    @Column(columnDefinition = "TEXT")
    private String email;

    @Builder
    public User(String nickName, String kakaoId) {
        this.nickName = nickName;
        this.kakaoId = kakaoId;
    }

}
