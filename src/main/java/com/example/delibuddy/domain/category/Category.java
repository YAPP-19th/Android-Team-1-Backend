package com.example.delibuddy.domain.category;

import com.example.delibuddy.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30) // todo: unique True 걸까...?
    private String name;

    public Category(String name) {
        this.name = name;
    }

}
