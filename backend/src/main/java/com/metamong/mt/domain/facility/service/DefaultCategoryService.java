package com.metamong.mt.domain.facility.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.metamong.mt.domain.facility.dto.response.CategoryListResponseDto;
import com.metamong.mt.domain.facility.model.Category;
import com.metamong.mt.domain.facility.repository.jpa.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultCategoryService implements CategoryService {
    private final CategoryRepository categoryRepository;
    
    @Override
    public List<CategoryListResponseDto> getAllCategories() {
        List<Category> allCategories = this.categoryRepository.findAll();
        
        Map<String, CategoryListResponseDto> cache = new HashMap<>();
        Map<String, Category> cache2 = new HashMap<>();
        List<CategoryListResponseDto> result = new ArrayList<>();
        for (Category category : allCategories) {
            CategoryListResponseDto dto = CategoryListResponseDto.of(category);
            if (category.isRoot()) {
                result.add(dto);
            }
            String catId = category.getCatId();
            cache.put(catId, dto);
            cache2.put(catId, category);
        }
        
        for (Map.Entry<String, CategoryListResponseDto> entry : cache.entrySet()) {
            Category category = cache2.get(entry.getKey());
            if (!category.isRoot()) {
                String parentId = cache2.get(entry.getKey()).getParentCat().getCatId();
                cache.get(parentId).addChild(entry.getValue());
            }
        }
        return result;
    }
}
