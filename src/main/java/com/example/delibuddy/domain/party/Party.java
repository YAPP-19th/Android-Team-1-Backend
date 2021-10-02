package com.example.delibuddy.domain.party;

import com.example.delibuddy.domain.BaseTimeEntity;
import com.example.delibuddy.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(fetch = FetchType.LAZY)
    private List<User> members;

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

    public boolean isJoinable() {
        return true;
    }

}
