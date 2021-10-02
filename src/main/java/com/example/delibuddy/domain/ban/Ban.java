package com.example.delibuddy.domain.ban;

import com.example.delibuddy.domain.BaseTimeEntity;
import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.user.User;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(
        name="bans",
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


}
