package com.metamong.mt.domain.reservation.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Reservation {
	@Id
	private int rvtId;
	
	private int consId;
	private int zoneId;
	private LocalDate rvtDate;
	private LocalDate usageStartTime;
	private LocalDate usageEndTime;
	private int usageCount;
	private LocalDate createdAt;
	private String rvtCancelationReason;
}
