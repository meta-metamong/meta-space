package com.metamong.mt.domain.payment.service;

import java.util.List;

import com.metamong.mt.domain.payment.dto.request.PaymentRequestDto;
import com.metamong.mt.domain.payment.dto.response.PaymentResponseDto;

public interface PaymentService {
    void savePayment(PaymentRequestDto dto);
    List<PaymentResponseDto> getPayments();
}
