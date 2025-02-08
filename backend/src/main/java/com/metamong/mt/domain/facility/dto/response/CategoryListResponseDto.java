package com.metamong.mt.domain.facility.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.metamong.mt.domain.facility.model.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class CategoryListResponseDto {
    private final String catId;
    private final String catName;
    private final List<CategoryListResponseDto> children;
    
    public static CategoryListResponseDto of(Category category) {
        return CategoryListResponseDto.builder()
                .catId(category.getCatId())
                .catName(category.getCatName())
                .children(new ArrayList<>())
                .build();
    }
    
    public void addChild(CategoryListResponseDto child) {
        this.children.add(child);
    }
}
