package com.metamong.mt.domain.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metamong.mt.domain.payment.service.PaymentService;
import com.metamong.mt.global.apispec.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;
    
    @GetMapping
    public ResponseEntity<?> getPayments(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(BaseResponse.of(paymentService.getPayments(Long.valueOf(user.getUsername())), HttpStatus.OK, "결제 내역 조회가 완료되었습니다."));
    }
    
    @GetMapping("/{rvtId}")
    public ResponseEntity<?> getPayment(@PathVariable Long rvtId){
        return ResponseEntity.ok(BaseResponse.of(paymentService.getPayment(rvtId), HttpStatus.OK, "결제 내역 조회가 완료되었습니다."));
    }
}
