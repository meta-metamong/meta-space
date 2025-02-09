package com.metamong.mt.domain.payment.service;

import java.util.List;

import com.metamong.mt.domain.payment.dto.request.PaymentRequestDto;
import com.metamong.mt.domain.payment.dto.response.PaymentResponseDto;

public interface PaymentService {
    
    /**
     * 회원 아이디를 통해 결제 내역을 목록으로 조회하는 메서드입니다.
     * @param memId 회원 ID(본인)
     * @return List<PaymentResponseDto> 결제 내역 목록
     */
    List<PaymentResponseDto> getPayments(Long memId);
    
    /**
     * 예약 아이디를 통해 결제 상세 내역을 조회하는 메서드입니다.
     * @param rvtId 예약 ID
     * @return PaymentResponseDto 결제 상세정보
     */
    PaymentResponseDto getPayment(Long rvtId);
}
