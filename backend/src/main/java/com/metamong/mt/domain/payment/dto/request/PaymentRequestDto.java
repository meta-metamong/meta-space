package com.metamong.mt.domain.payment.dto.request;

import java.time.LocalDateTime;

import com.metamong.mt.domain.payment.model.Payment;
import com.metamong.mt.domain.payment.model.constant.PaymentState;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentRequestDto {

    @NotEmpty(message="결제 금액은 필수입니다.")
    private Long payPrice;
    
    @NotEmpty(message="결제 방법은 필수입니다.")
    private String payMethod;
    
    
    public Payment toEntity() {
        return Payment.builder()
                      .payPrice(this.payPrice)
                      .payMethod(this.payMethod)
                      .payState(PaymentState.P)
                      .payDate(LocalDateTime.now())
                      .build();
    }
}
