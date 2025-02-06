package com.metamong.mt.domain.facility.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private boolean isOpenOnHolidays;
    private LocalDateTime fctOpenTime;
    private LocalDateTime fctCloseTime;
    private int unitUsageTime;
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Builder.Default
    private FacilityState fctState = FacilityState.REG_REQUESTED;
    
    private double fctLatitude;
    private double fctLongitude;
}
