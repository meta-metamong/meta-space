package com.metamong.mt.domain.payment.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.metamong.mt.domain.payment.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
