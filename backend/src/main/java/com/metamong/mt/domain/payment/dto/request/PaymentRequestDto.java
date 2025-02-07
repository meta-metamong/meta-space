package com.metamong.mt.domain.payment.dto.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PaymentRequestDto {
    private Long rvtId;
    private Long payPrice;
    private String payState;
    private String payMethod;
    private LocalDateTime payDate;
    private LocalDateTime cancelDate;
    private String refundBankCode;
    private String refundAccount;
    private String refundAccountOwner;
}
