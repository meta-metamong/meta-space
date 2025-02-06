package com.metamong.mt.domain.facility.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Category {

    @Id
    private String catId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_cat_id")
    private Category parentCat;
    
    private String catName;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentCat")
    private List<Category> children = new ArrayList<>();
    
    public Category(String catId) {
        this.catId = catId;
    }
    
    public Category(String catId, String catName) {
        this.catId = catId;
        this.catName = catName;
    }
    
    public Category(String catId, String catName, Category parentCat) {
        this.catId = catId;
        this.parentCat = parentCat;
        this.catName = catName;
    }
    
    public void addChildCategory(Category childCategory) {
        this.children.add(childCategory);
    }
}
