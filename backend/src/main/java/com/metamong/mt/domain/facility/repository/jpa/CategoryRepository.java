package com.metamong.mt.domain.facility.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.metamong.mt.domain.facility.model.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {

}
