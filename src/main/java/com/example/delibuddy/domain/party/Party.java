package com.example.delibuddy.domain.party;

import com.example.delibuddy.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Party extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
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
}
