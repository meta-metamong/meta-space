package com.metamong.mt.domain.payment.dto.response;

import java.time.LocalDateTime;

import com.metamong.mt.domain.payment.model.constant.PaymentState;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class PaymentResponseDto {
    private Long rvtId;
    private Long payPrice;
    private String payMethod;
    private PaymentState payState;
    private LocalDateTime payDate;
    private String refundBankCode;
    private String refundAccount;
    private String refundAccountOwner;
}
