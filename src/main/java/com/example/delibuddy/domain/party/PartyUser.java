package com.example.delibuddy.domain.party;

import com.example.delibuddy.domain.BaseTimeEntity;
import com.example.delibuddy.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@Getter
@Entity
@Table(
        name="party_user",
        uniqueConstraints={
                @UniqueConstraint(
                        columnNames={"user_id","party_id"}
                )
        }
)
public class PartyUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    @Builder
    public PartyUser(User user, Party party) {
        this.user = user;
        this.party = party;
    }
}
