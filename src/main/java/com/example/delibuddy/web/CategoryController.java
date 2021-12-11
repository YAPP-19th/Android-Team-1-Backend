package com.example.delibuddy.web;

import com.example.delibuddy.service.CategoryService;
import com.example.delibuddy.web.dto.CategoryResponseDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "Category")
@RequiredArgsConstructor
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("${api.v1}/categories")
    public List<CategoryResponseDto>  getAllCategories() {
        return categoryService.getAllCategories();
    }
}
