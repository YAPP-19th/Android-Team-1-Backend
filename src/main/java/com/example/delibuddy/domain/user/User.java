package com.example.delibuddy.domain.user;

import com.example.delibuddy.domain.BaseTimeEntity;
import com.example.delibuddy.domain.party.PartyUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String nickName;

    @Column(length = 255, nullable = false, unique = true)
    private String kakaoId;

    @Column(length = 255)
    private String email;

    @Column(length = 255)
    private String profileImage;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<PartyUser> parties = new ArrayList<>();

    @Builder
    public User(String nickName, String kakaoId, String email, String profileImage) {
        this.nickName = nickName;
        this.kakaoId = kakaoId;
        this.email = email;
        this.profileImage = profileImage;
    }

}
