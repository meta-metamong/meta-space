package com.metamong.mt.domain.payment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.metamong.mt.domain.payment.dto.response.PaymentResponseDto;
import com.metamong.mt.domain.payment.repository.mybatis.PaymentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultPaymentService implements PaymentService{
    
    private final PaymentMapper paymentMapper;

    @Override
    public List<PaymentResponseDto> getPayments(Long memId) {
        return this.paymentMapper.findPaymentsByMemberId(memId);
    }

    @Override
    public PaymentResponseDto getPayment(Long rvtId) {
        return this.paymentMapper.findPaymentByReservationId(rvtId);
    }

}
