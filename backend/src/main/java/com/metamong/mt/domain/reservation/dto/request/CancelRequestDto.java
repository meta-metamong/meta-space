package com.metamong.mt.domain.reservation.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CancelRequestDto {
    @NotEmpty(message="예약 취소 이유는 필수입니다.")
    private String rvtCancelationReason;
    
    @NotEmpty(message="은행 코드는 필수입니다.")
    private String refundBankCode;
    
    @NotEmpty(message=" 이유는 필수입니다.")
    private String refundAccount;
    
    @NotEmpty(message="예약 취소 이유는 필수입니다.")
    private String refundAccountOwner;
}
