package com.metamong.mt.domain.facility.service;

import java.util.List;

import com.metamong.mt.domain.facility.dto.response.CategoryListResponseDto;

public interface CategoryService {

    List<CategoryListResponseDto> getAllCategories();
}
