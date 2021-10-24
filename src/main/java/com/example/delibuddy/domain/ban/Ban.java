package com.example.delibuddy.domain.ban;

import com.example.delibuddy.domain.BaseTimeEntity;
import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name="ban",
        uniqueConstraints={
                @UniqueConstraint(
                        columnNames={"user_id","party_id"}
                )
        }
)
public class Ban extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    // TODO: 아니 연관관계 매핑 말고 그냥 FK 만 추가할 순 없나..? 연관 관계 맺으려면 객체를 다 불러와야되?
//    private Long party_id;

    @Builder
    public Ban(User user, Party party) {
        this.user = user;
        this.party = party;
    }
}
