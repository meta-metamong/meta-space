package com.metamong.mt.domain.member.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.metamong.mt.domain.member.dto.request.UpdateRequestDto;
import com.metamong.mt.domain.member.model.constant.Gender;
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
    private Long memId;
    
    private String email;
    private String memName;
    private String password;
    private String memPhone;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime birthDate;
    private String memPostalCode;
    private String memDetailAddress;
    private String memAddress;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    private String refreshToken;
    private LocalDateTime createdAt = null;
    private LocalDateTime updatedAt = null;
    private LocalDateTime memBannedUntil = null;
    
    public void updateInfo(UpdateRequestDto dto) {
        this.memName = dto.getMemName();
        this.memPhone = dto.getMemPhone();
        this.gender = Gender.valueOf(dto.getGender());
        this.birthDate = dto.getBirthDate();
        this.memPostalCode = dto.getMemPostalCode();
        this.memAddress = dto.getMemAddress();
        this.memDetailAddress = dto.getMemDetailAddress();
    }
    
    public void changePassword(String password) {
        this.password = password;
    }
}