package com.example.delibuddy.domain.party;

import com.example.delibuddy.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import javax.persistence.*;

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

    @Builder
    public Party(Point coordinate, String title, String body) {
        this.coordinate = coordinate;
        this.title = title;
        this.body = body;
    }
}
