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

    @Column(length = 200)
    private String iconUrl;

    @Column(length = 30)
    private String backgroundColorCode;

    public Category(String name, String code, String iconUrl, String backgroundColorCode) {
        this.name = name;
        this.code = code;
        this.iconUrl = iconUrl;
        this.backgroundColorCode = backgroundColorCode;
    }

}
