package com.metamong.mt.domain.facility.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.metamong.mt.global.constant.BooleanAlt;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "zone")
@SequenceGenerator(
        name = "zone_pk_generator",
        sequenceName = "zone_pk_seq",
        initialValue = 1,
        allocationSize = 1
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "zone_pk_generator")
    private Long zoneId;
    
    private Long fctId;
    private String zoneName;
    private Integer maxUserCount;
    
    @Enumerated(EnumType.STRING)
    private BooleanAlt isSharedZone;
    
    private Integer hourlyRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Builder.Default
    @OneToMany(mappedBy = "zone", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ZoneImage> images = new ArrayList<>();
    
    public void addZoneImage(ZoneImage zoneImage) {
        this.images.add(zoneImage);
    }
}
