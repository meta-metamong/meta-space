package com.metamong.mt.domain.facility.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metamong.mt.domain.facility.dto.response.CategoryListResponseDto;
import com.metamong.mt.domain.facility.model.Category;
import com.metamong.mt.domain.facility.repository.jpa.CategoryRepository;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class DefaultCategoryServiceTest {

    @InjectMocks
    DefaultCategoryService categoryService;
    
    @Mock
    CategoryRepository categoryRepository;
    
    @Test
    @DisplayName("getAllCategories() - success")
    void getAllCategories_success() throws JsonProcessingException {
        // Given
        Category root1 = new Category("100", "Root1");
        Category root2 = new Category("200", "Root2");
        Category intermediate = new Category("220", "intermediate", root2);
        List<Category> categories = List.of(
                new Category("101", "cat1_1", root1),
                new Category("102", "cat1_2", root1),
                new Category("103", "cat1_3", root1),
                new Category("104", "cat1_4", root1),
                new Category("201", "cat2_1", root2),
                new Category("202", "cat2_2", root2),
                new Category("203", "cat2_3", root2),
                new Category("204", "cat2_4", root2),
                new Category("205", "cat2_5", root2),
                root1,
                root2,
                intermediate,
                new Category("221", "leaf", intermediate)
        );
        when(this.categoryRepository.findAll())
                .thenReturn(categories);
        
        // When
        List<CategoryListResponseDto> result = this.categoryService.getAllCategories();
        
        ObjectMapper ob = new ObjectMapper();
        log.info("result:\n{}", ob.writeValueAsString(result));
        
        // Then
        assertThat(result).size().isEqualTo(2);
        
        for (CategoryListResponseDto root : result) {
            if (root.getCatId().equals("100")) {
                assertThat(root.getChildren().stream().map(CategoryListResponseDto::getCatId))
                        .containsExactlyInAnyOrder("101", "102", "103", "104");
            } else if (root.getCatId().equals("200")) {
                assertThat(root.getChildren().stream().map(CategoryListResponseDto::getCatId))
                        .containsExactlyInAnyOrder("201", "202", "203", "204", "205", "220");
                
                CategoryListResponseDto inter = root.getChildren().stream()
                        .filter((e) -> e.getCatId().equals("220"))
                        .findAny()
                        .orElseThrow(RuntimeException::new);
                assertThat(inter.getChildren()).size().isEqualTo(1);
                assertThat(inter.getChildren().get(0).getCatId()).isEqualTo("221");
            }
        }
    }
}
