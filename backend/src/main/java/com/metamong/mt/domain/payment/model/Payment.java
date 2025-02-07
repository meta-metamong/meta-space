package com.metamong.mt.domain.payment.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.metamong.mt.domain.reservation.model.Reservation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="payment")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Payment {
    
    @Id
    private Long rvtId;
    
    @Column(name="pay_price")
    private Long payPrice;
    
    @Column(name="pay_state")
    private Character payState;
    
    @Column(name="pay_method")
    private String payMethod;
    
    @Column(name="pay_date")
    @ColumnDefault(value="SYSDATE")
    private LocalDateTime payDate;
    
    @Column(name="cancel_date")
    private LocalDateTime cancel_date;
    
    @Column(name="refund_bank_code")
    private String refundBankCode;
    
    @Column (name="refund_account")
    private String refundAccount;
    
    @Column(name="refund_account_owner")
    private String refundAccountOwner;

}
