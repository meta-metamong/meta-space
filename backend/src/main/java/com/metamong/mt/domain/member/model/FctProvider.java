package com.metamong.mt.domain.member.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "fct_provider")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FctProvider {
    
    @Id
    private Long provId;
    
    @OneToOne
    @MapsId
    @JoinColumn(name="mem_id")
    private Member member;
    
    @Column(name="bank_code")
    private String bankCode;
    
    @Column(name="biz_name")
    private String bizName;
    
    @Column(name="biz_reg_num")
    private String bizRegNum;
    
    @Column(name="prov_account")
    private String provAccount;
    
    @Column(name="prov_account_owner")
    private String provAccountOwner;
    
    public void updateInfo(FctProvider provider) {
        this.bankCode = provider.getBankCode();
        this.bizName = provider.getBizName();
        this.bizRegNum = provider.getBizRegNum();
        this.provAccount = provider.getProvAccount();
        this.provAccountOwner = provider.getProvAccountOwner();
    }
}
