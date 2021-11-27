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

    @Column(length = 30)
    private String name;

    @Column(length = 30, unique = true)
    private String code;

    public Category(String name, String code) {
        this.name = name;
        this.code = code;
    }

}
