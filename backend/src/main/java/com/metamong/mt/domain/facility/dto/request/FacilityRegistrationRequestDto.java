package com.metamong.mt.domain.facility.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import com.metamong.mt.domain.facility.model.Category;
import com.metamong.mt.domain.facility.model.Facility;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FacilityRegistrationRequestDto {
    private String fctName;
    private String fctPostalCode;
    private String fctAddress;
    private String fctDetailAddress;
    private Long catId;
    private Long provId;
    private String fctTel;
    private String fctGuide;
    private boolean openOnHolidays;
    private LocalDateTime fctOpenTime;
    private LocalDateTime fctCloseTime;
    private int unitUsageTime;
    private List<ImageRequestDto> images;
    private List<ZoneRegistrationRequestDto> zones;
    private List<String> addinfos;
    
    public Facility toEntity() {
        return Facility.builder()
                .cat(new Category(this.getCatId()))
                .provId(this.getProvId())
                .fctName(this.getFctName())
                .fctPostalCode(this.getFctPostalCode())
                .fctAddress(this.getFctAddress())
                .fctDetailAddress(this.getFctDetailAddress())
                .fctTel(this.getFctTel())
                .fctGuide(this.getFctGuide())
                .openOnHolidays(this.isOpenOnHolidays())
                .fctOpenTime(this.getFctOpenTime())
                .fctCloseTime(this.getFctCloseTime())
                .unitUsageTime(this.getUnitUsageTime())
                // TODO: fctLatitude, fctLongitude
                .build();
    }
}
