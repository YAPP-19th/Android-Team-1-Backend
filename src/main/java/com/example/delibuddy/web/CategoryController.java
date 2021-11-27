package com.example.delibuddy.web;


import com.example.delibuddy.domain.category.CategoryRepository;
import com.example.delibuddy.web.dto.CategoryResponseDto;
import com.example.delibuddy.web.dto.PartyResponseDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "Category")
@RequiredArgsConstructor
@RestController
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("${api.v1}/categories")
    public List<CategoryResponseDto> getCategories() {
        return categoryRepository.findAll().stream()
            .map(CategoryResponseDto::new)
            .collect(Collectors.toList());
    }

}
