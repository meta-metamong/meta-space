package com.metamong.mt.global.image.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("F")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FacilityImage extends Image {
    private Long fctId;
    
    public FacilityImage(String imgPath, Integer imgDisplayOrder, Long fctId) {
        super(imgPath, imgDisplayOrder);
        this.fctId = fctId;
    }
}
