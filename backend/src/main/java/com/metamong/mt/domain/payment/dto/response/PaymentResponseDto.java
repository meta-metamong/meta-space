package com.metamong.mt.domain.payment.dto.response;

import java.time.LocalDateTime;

import com.metamong.mt.domain.reservation.dto.constant.PayState;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class PaymentResponseDto {
    private String payPrice;
    private PayState payState;
    private String payMethod;
    private LocalDateTime payDate;
    private LocalDateTime cancelDate;
    private String refundBankCode;
    private String refundAccount;
    private String refundAccountOwner;
}
