package com.metamong.mt.domain.reservation.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.metamong.mt.domain.payment.model.Payment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "reservation")
@SequenceGenerator(
        name = "rvt_pk_generator",
        sequenceName = "rvt_pk_seq",
        initialValue = 1,
        allocationSize = 1
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter @Setter
@ToString
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rvt_pk_generator")
    private Long rvtId;

    private Long consId;
    private Long zoneId;
    private LocalDate rvtDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime usageStartTime;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime usageEndTime;
    private int usageCount;
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    private String rvtCancelationReason;
    
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;
}
