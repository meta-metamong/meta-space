package com.metamong.mt.domain.payment.service;

import java.util.List;

import com.metamong.mt.domain.payment.dto.response.PaymentResponseDto;
import com.metamong.mt.domain.payment.model.Payment;
import com.metamong.mt.domain.reservation.dto.request.CancelRequestDto;

public interface PaymentService {
    
    /**
     * 결제 정보를 저장하고, 판매자 계좌에 입금하는 메서드입니다.
     * @param rvtId 예약 ID
     * @param Payment 결제 정보를 담은 엔티티
     */
    void savePayment(Long rvtId, Payment paymentDto);
    
    /**
     * 결제 내역을 목록으로 조회하는 메서드입니다.
     * @param memId 회원 ID(본인)
     * @return List<PaymentResponseDto> 결제 내역 목록
     */
    List<PaymentResponseDto> getPayments(Long memId);
    
    /**
     * 결제 상세 내역을 반환하는 메서드입니다.
     * @param rvtId 예약 ID
     * @return PaymentResponseDto 결제 상세정보
     */
    PaymentResponseDto getPayment(Long rvtId);
    
    /**
     * 예약 취소시 환불 처리하는 메서드입니다.
     * @param rvtId
     */
    void reservationCancelRequest(Long rvtId, CancelRequestDto dto);
    
    /**
     * 관리자가 환불 허가시 환불하는 메서드입니다.
     * @param rvtId 예약 ID
     * @return 환불된 금액
     */
    Long refund(Long rvtId);
    
    void noRefund(Long rvtId);
}
