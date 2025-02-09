package com.metamong.mt.domain.member.model;

import com.metamong.mt.global.constant.BooleanAlt;

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
@Table(name = "account")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Account {    
    @Id
    @Column(name="prov_id")
    private Long provId;
    
    @Column(name="bank_code")
    private String bankCode;
    
    @Column(name="account_number")
    private String accountNumber;
    
    @Column(name="balance")
    private Long balance;
    
    @Column(name="is_agreed_info")
    @Enumerated(EnumType.STRING)
    private BooleanAlt isAgreedInfo;
    
    public void updateInfo(Account dto) {
        this.bankCode = dto.bankCode;
        this.accountNumber = dto.getAccountNumber();
    }
    
    public Long updateBalance(Long amount) {
        this.balance += amount;
        return this.balance;
    }
}
