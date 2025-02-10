package com.metamong.mt.domain.payment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.metamong.mt.domain.member.model.Account;
import com.metamong.mt.domain.member.repository.jpa.AccountRepository;
import com.metamong.mt.domain.payment.dto.response.PaymentResponseDto;
import com.metamong.mt.domain.payment.exception.AccountNotFoundException;
import com.metamong.mt.domain.payment.exception.NotEnoughMoneyException;
import com.metamong.mt.domain.payment.exception.PaymentNotFoundException;
import com.metamong.mt.domain.payment.model.Payment;
import com.metamong.mt.domain.payment.model.constant.PaymentState;
import com.metamong.mt.domain.payment.repository.jpa.PaymentRepository;
import com.metamong.mt.domain.payment.repository.mybatis.PaymentMapper;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultPaymentService implements PaymentService{
    
    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;
    
    @Override
    @Transactional
    public void savePayment(Long rvtId, Payment paymentDto) {
        paymentDto.setRvtId(rvtId);
        this.paymentRepository.save(paymentDto);
        
        Account account = this.getAccountByReservationId(rvtId);
        account.updateBalance(paymentDto.getPayPrice());
        accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getPayments(Long memId) {
        return this.paymentMapper.findPaymentsByMemberId(memId);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPayment(Long rvtId) {
        return this.paymentMapper.findPaymentByReservationId(rvtId);
    }
    
    @Override
    public void reservationCancelRequest(Long rvtId) {
        this.getPaymentByRepository(rvtId).reservationCancelRequest();
    }

    @Override
    @Transactional
    public Long refund(Long rvtId) {
        Payment payment = this.getPaymentByRepository(rvtId);
        payment.setPayState(PaymentState.R);
        Account account = this.getAccountByReservationId(rvtId);
        if(account.getBalance() < payment.getPayPrice()) {
            throw new NotEnoughMoneyException();
        }
        
        account.updateBalance(payment.getPayPrice() * -1);
        accountRepository.save(account);
        return payment.getPayPrice();
    }
    
    @Transactional(readOnly=true)
    private Payment getPaymentByRepository(Long rvtId) {
        return this.paymentRepository.findById(rvtId).orElseThrow(() -> new PaymentNotFoundException());
    }
    
    @Transactional(readOnly=true)
    private Account getAccountByReservationId(Long rvtId) {
        Account account = this.paymentMapper.findAccountByReservationId(rvtId);
        if(account == null) {
            throw new AccountNotFoundException();
        }
        return account;
    }
}
