package com.metamong.mt.domain.facility.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.metamong.mt.global.constant.BooleanAlt;
import com.metamong.mt.global.location.LatLng;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "facility")
@SequenceGenerator(
        name = "fct_pk_generator",
        sequenceName = "fct_pk_seq",
        initialValue = 1,
        allocationSize = 1
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fct_pk_generator")
    private Long fctId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    private Category cat;
    
    private Long provId;
    private String fctName;
    private String fctPostalCode;
    private String fctAddress;
    private String fctDetailAddress;
    private String fctTel;
    private String fctGuide;
    
    @Column(name = "is_open_on_holidays")
    @Enumerated(EnumType.STRING)
    private BooleanAlt openOnHolidays;
    
    private LocalTime fctOpenTime;
    private LocalTime fctCloseTime;
    private int unitUsageTime;
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private FacilityState fctState = FacilityState.REG_REQUESTED;
    
    private double fctLatitude;
    private double fctLongitude;
    
    @OneToMany(mappedBy = "fct", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @Builder.Default
    private List<FacilityImage> fctImages = new ArrayList<>();
    
    public void addFctImage(FacilityImage fctImage) {
        this.fctImages.add(fctImage);
    }
    
    public void requestDelete() {
        this.fctState = FacilityState.DEL_REQUESTED;
    }
    
    public void updateLatLng(LatLng latLng) {
        this.fctLatitude = latLng.getLatitude();
        this.fctLongitude = latLng.getLongitude();
    }
}
