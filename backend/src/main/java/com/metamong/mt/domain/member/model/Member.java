package com.metamong.mt.domain.member.model;

import java.time.LocalDate;

import com.metamong.mt.domain.member.dto.request.UserUpdateRequestDto;
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
    
    public void updateInfo(UserUpdateRequestDto dto) {
        this.name = dto.getName();
        this.phone = dto.getPhone();
        this.email = dto.getEmail();
        this.birth = dto.getBirth();
        this.postalCode = dto.getPostalCode();
        this.address = dto.getAddress();
        this.detailAddress = dto.getDetailAddress();

        // 비밀번호 변경이 있을 경우만 업데이트
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            this.password = dto.getPassword();
        }
    }
}