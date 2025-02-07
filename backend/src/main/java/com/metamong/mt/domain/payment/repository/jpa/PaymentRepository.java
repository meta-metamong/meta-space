package com.metamong.mt.domain.payment.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.payment.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
