package com.metamong.mt.domain.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacilityReservationResponseDto {
    private String facilityName;
    private Long facilityId;
    private Long totalReservations;
    private Long cancelledReservations;
    private Long totalRevenue;
}
