package com.metamong.mt.domain.facility.model;

import com.metamong.mt.global.image.model.Image;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("F")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FacilityImage extends Image {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fct_id")
    private Facility fct;
    
    public FacilityImage(String imgPath, Integer imgDisplayOrder, Facility fct) {
        super(imgPath, imgDisplayOrder);
        this.fct = fct;
    }

    @Override
    public String toString() {
        return "FacilityImage [" + super.toString() + ", fct.fctId=" + fct.getFctId() + "]";
    }
}
