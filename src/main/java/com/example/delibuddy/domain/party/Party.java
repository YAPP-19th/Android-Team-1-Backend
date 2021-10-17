package com.example.delibuddy.domain.party;

import com.example.delibuddy.domain.BaseTimeEntity;
import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.web.dto.PartyResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Entity
public class Party extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private User leader;

    @OneToMany(mappedBy = "party", fetch = FetchType.LAZY)
    private List<PartyUser> users = new ArrayList<>();

    @Column(columnDefinition = "CHAR(255)")
    private String title;

    @Column(columnDefinition = "VARCHAR(1000)")
    private String body;

    @Column
    private Point coordinate;

    // TODO: enum status 만들기

    @Column
    private PartyStatus status;

    @Column
    private LocalDateTime orderTime;

    @Builder
    public Party(Point coordinate, String title, String body, LocalDateTime orderTime) {
        this.coordinate = coordinate;
        this.title = title;
        this.body = body;
        this.orderTime = orderTime;
        this.status = PartyStatus.OPEN;
    }

    public boolean isIn(User user) {
        return !users.stream().map(PartyUser::getUser).anyMatch(u -> u.getId().equals(user.getId()));
    }

    public void join(PartyUser partyUser) {
        // TODO: 아니 이걸 해줘야되?? PartyUser 를 생성해서 flush 하면 자동으로 감지될 수는 없나?
        users.add(partyUser);
        partyUser.getUser().getParties().add(partyUser);
    }

}
