package com.metamong.mt.domain.payment.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.metamong.mt.domain.payment.model.constant.PaymentState;
import com.metamong.mt.domain.reservation.dto.request.CancelRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="payment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
@ToString
public class Payment {
    
    @Id
    private Long rvtId;
    
    @Column(name="pay_price")
    private Long payPrice;
    
    @Column(name="pay_method")
    private String payMethod;
    
    @Column(name="pay_state")
    @Enumerated(EnumType.STRING)
    private PaymentState payState;
    
    @Column(name="pay_date")
    @ColumnDefault(value="SYSDATE")
    private LocalDateTime payDate;
    
    @Column(name="cancel_date")
    private LocalDateTime cancelDate;
    
    @Column(name="refund_bank_code")
    private String refundBankCode;
    
    @Column (name="refund_account")
    private String refundAccount;
    
    @Column(name="refund_account_owner")
    private String refundAccountOwner;

    public void reservationCancelRequest(CancelRequestDto dto) {
        this.payState = PaymentState.Q;
        this.cancelDate = LocalDateTime.now();
        this.refundBankCode = dto.getRefundBankCode();
        this.refundAccount = dto.getRefundAccount();
        this.refundAccountOwner = dto.getRefundAccountOwner();
    }
}
