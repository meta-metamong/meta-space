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
@DiscriminatorValue("Z")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ZoneImage extends Image {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;
    
    public ZoneImage(String imgPath, Integer imgDisplayOrder, Zone zone) {
        super(imgPath, imgDisplayOrder);
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "ZoneImage [" + super.toString() + ", zone.zoneId=" + zone.getZoneId() + "]";
    }
}
