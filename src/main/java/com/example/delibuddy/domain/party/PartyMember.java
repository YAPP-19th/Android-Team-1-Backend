package com.example.delibuddy.domain.party;

import com.example.delibuddy.domain.BaseTimeEntity;
import com.example.delibuddy.domain.user.User;

import javax.persistence.*;

@Entity
@Table(
        name="party_members",
        uniqueConstraints={
                @UniqueConstraint(
                        columnNames={"user_id","party_id"}
                )
        }
)
public class PartyMember extends BaseTimeEntity { // TODO 얘는 왜 테이블에 s 가 붙지...?

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

}
