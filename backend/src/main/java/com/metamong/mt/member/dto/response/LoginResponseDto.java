package com.metamong.mt.member.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private String userId;
    private String name;
    private String phone;
    private String email;
    private String role;

    // Constructor
    public LoginResponseDto(String userId, String name, String phone, String email, String role) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.role = role;
    }
}
