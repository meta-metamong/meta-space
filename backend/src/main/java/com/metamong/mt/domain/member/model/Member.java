package com.metamong.mt.domain.member.model;

import java.time.LocalDate;

import com.metamong.mt.domain.member.dto.request.UpdateRequestDto;
import com.metamong.mt.domain.member.model.constant.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long memberId;
    
    private String email;
    private String name;
    private String password;
    private String phone;
    private String gender;
    private LocalDate birthDate;
    private String postalCode;
    private String detailAddress;
    private String address;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    private String refreshToken;
    
    private String businessName;
    private String businessNumber;
    private String bankCode;
    private String account;
    private String accountOwner;
    
    public void updateInfo(UpdateRequestDto dto) {
    	this.email = dto.getEmail();
        this.name = dto.getName();
        this.phone = dto.getPhone();
        this.gender = dto.getGender();
        this.birthDate = dto.getBirthDate();
        this.postalCode = dto.getPostalCode();
        this.address = dto.getAddress();
        this.detailAddress = dto.getDetailAddress();
        
        if(this.role.equals(Role.ROLE_PROV)) {
        	this.businessName = dto.getBusinessName();
        	this.businessNumber = dto.getBusinessNumber();
        	this.bankCode = dto.getBankCode();
        	this.account = dto.getAccount();
        	this.accountOwner = dto.getAccountOwner();
        }
    }
}