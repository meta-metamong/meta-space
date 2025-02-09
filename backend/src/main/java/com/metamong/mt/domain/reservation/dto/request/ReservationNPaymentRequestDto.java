package com.metamong.mt.domain.reservation.dto.request;

import com.metamong.mt.domain.payment.dto.request.PaymentRequestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ReservationNPaymentRequestDto {
    
    ReservationRequestDto reservation;
    
    PaymentRequestDto payment;
}
