package com.example.delibuddy.domain.party;

import com.example.delibuddy.domain.BaseTimeEntity;
import com.example.delibuddy.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    @OnDelete(action = OnDeleteAction.CASCADE)  // https://stackoverflow.com/questions/14875793/jpa-hibernate-how-to-define-a-constraint-having-on-delete-cascade
    private Party party;

    @Builder
    public PartyUser(User user, Party party) {
        this.user = user;
        this.party = party;
    }
}
