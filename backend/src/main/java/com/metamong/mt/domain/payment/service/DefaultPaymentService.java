package com.metamong.mt.domain.payment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.metamong.mt.domain.payment.dto.request.PaymentRequestDto;
import com.metamong.mt.domain.payment.dto.response.PaymentResponseDto;

@Service
public class DefaultPaymentService implements PaymentService{

    @Override
    public void savePayment(PaymentRequestDto dto) {
        
    }

    @Override
    public List<PaymentResponseDto> getPayments() {
        return null;
    }

}
