package com.metamong.mt.member.model;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class Member {
    private String userid;
    private String name;
    private String password;
    private String phone;
    private String email;
    private LocalDate birth;
    private String postal_code;
    private String detail_address;
    private String address;
    private String role;
    private String refreshToken;
    private String businessName;
    private String businessRegistrationNumber;
}