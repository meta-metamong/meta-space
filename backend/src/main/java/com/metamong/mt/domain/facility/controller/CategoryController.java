package com.metamong.mt.domain.facility.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metamong.mt.domain.facility.dto.response.CategoryListResponseDto;
import com.metamong.mt.domain.facility.service.CategoryService;
import com.metamong.mt.global.apispec.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    
    @GetMapping("/categories")
    public ResponseEntity<BaseResponse<List<CategoryListResponseDto>>> getAllCategories() {
        return ResponseEntity.ok(
                BaseResponse.of(this.categoryService.getAllCategories(), HttpStatus.OK)
        );
    }
}
