package com.metamong.mt.domain.member.model;

import java.time.LocalDate;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.metamong.mt.domain.member.model.constant.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private String userId;
    
    private String name;
    private String password;
    private String phone;
    private String email;
    private LocalDate birth;
    private String postalCode;
    private String detailAddress;
    private String address;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    private String refreshToken;
    private String businessName;
    private String businessRegistrationNumber;
}