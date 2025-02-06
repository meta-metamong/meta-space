package com.metamong.mt.domain.reservation.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class Reservation {
    @Id
    private Long rvtId;

    private Long consId;
    private Long zoneId;
    private LocalDate rvtDate;
    private LocalTime usageStartTime;
    private LocalTime usageEndTime;
    private int usageCount;
    private LocalDate createdAt;
    private String rvtCancelationReason;
}
