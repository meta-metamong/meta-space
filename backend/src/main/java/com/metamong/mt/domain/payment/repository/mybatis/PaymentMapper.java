package com.metamong.mt.domain.payment.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.payment.dto.response.PaymentResponseDto;

@Repository
@Mapper
public interface PaymentMapper {
    
    /**
     * 회원 아이디로 결제 목록 가져오는 쿼리입니다.
     * @param memId 회원 ID
     * @return List<PaymentResponseDto> 결제 응답 dto 리스트
     */
    List<PaymentResponseDto> findPaymentsByMemberId(Long memId);
    
    /**
     * 예약 아이디로 결제 상세 정보를 가져오는 쿼리입니다.
     * @param rvtId 예약 ID
     * @return PaymentResponseDto 결제 응답 dto
     */
    PaymentResponseDto findPaymentByReservationId(Long rvtId);
}
