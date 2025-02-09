package com.metamong.mt.domain.payment.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.member.model.Account;
import com.metamong.mt.domain.payment.dto.response.PaymentResponseDto;

@Repository
@Mapper
public interface PaymentMapper {
    
    /**
     * 결제 목록을 조회하는 쿼리입니다.
     * @param memId 회원 ID
     * @return List<PaymentResponseDto> 결제 응답 dto 리스트
     */
    List<PaymentResponseDto> findPaymentsByMemberId(Long memId);
    
    /**
     * 결제 상세 정보를 조회하는 쿼리입니다.
     * @param rvtId 예약 ID
     * @return PaymentResponseDto 결제 응답 dto
     */
    PaymentResponseDto findPaymentByReservationId(Long rvtId);
    
    /**
     * 판매자 계좌를 조회하는 쿼리입니다.
     * @param rvtId 예약 ID
     * @return Account 계좌 Entity
     */
    Account findAccountByReservationId(Long rvtId);
}
