package com.example.delibuddy.web.dto;


import com.example.delibuddy.domain.category.Category;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CategoryResponseDto {
    private final Long id;
    private final String name;
    private final String iconUrl;
    private final String backgroundColorCode;

    public CategoryResponseDto(Category category) {
        id = category.getId();
        name = category.getName();
        iconUrl = category.getIconUrl();
        backgroundColorCode = category.getBackgroundColorCode();
    }
}
