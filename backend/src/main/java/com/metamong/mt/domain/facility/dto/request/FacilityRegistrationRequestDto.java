package com.metamong.mt.domain.facility.dto.request;

import java.time.LocalTime;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.metamong.mt.domain.facility.model.Category;
import com.metamong.mt.domain.facility.model.Facility;
import com.metamong.mt.global.constant.BooleanAlt;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FacilityRegistrationRequestDto {
    
    @NotNull
    @NotEmpty
    private String fctName;
    
    @NotNull
    @Length(min = 5, max = 5)
    private String fctPostalCode;
    
    @NotNull
    @NotEmpty
    private String fctAddress;
    
    @NotNull
    @NotEmpty
    private String fctDetailAddress;
    
    @NotNull
    @Length(min = 3, max = 3)
    private String catId;
    
    @NotNull
    private Long provId;
    
    @NotNull
    @Pattern(regexp = "^(02|010|011|016|017)\\-\\d{3,4}\\-\\d{4}$")
    private String fctTel;
    
    @NotNull
    @NotEmpty
    private String fctGuide;
    
    private BooleanAlt openOnHolidays;
    
    @NotNull
    private LocalTime fctOpenTime;
    
    @NotNull
    private LocalTime fctCloseTime;
    
    @NotNull
    @Min(30)
    private int unitUsageTime;
    
    private List<ImageRequestDto> images;
    
    @NotNull
    @Size(min = 1)
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
                .openOnHolidays(this.getOpenOnHolidays())
                .fctOpenTime(this.getFctOpenTime())
                .fctCloseTime(this.getFctCloseTime())
                .unitUsageTime(this.getUnitUsageTime())
                // TODO: fctLatitude, fctLongitude
                .build();
    }
}
