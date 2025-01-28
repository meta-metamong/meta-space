package com.metamong.mt.member.dto.request;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OwnerSignRequestDto {
    private String userid;
    private String email;
    private String password;
    private String confirmPassword;
    private String name;
    private String phone;
    private String businessName;
    private String businessRegistrationNumber;
    private Date birth;
    private String postalCode;
    private String detailAddress;
    private String address;
}
