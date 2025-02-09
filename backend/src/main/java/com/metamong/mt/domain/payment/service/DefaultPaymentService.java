package com.metamong.mt.domain.payment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.metamong.mt.domain.payment.dto.request.PaymentRequestDto;
import com.metamong.mt.domain.payment.dto.response.PaymentResponseDto;
import com.metamong.mt.domain.payment.repository.jpa.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultPaymentService implements PaymentService{
    
    private final PaymentRepository paymentRepository;

    @Override
    public void savePayment(PaymentRequestDto dto) {

    }

    @Override
    public List<PaymentResponseDto> getPayments() {
        return null;
    }

}
