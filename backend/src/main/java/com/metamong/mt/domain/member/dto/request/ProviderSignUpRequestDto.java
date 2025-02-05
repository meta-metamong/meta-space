package com.metamong.mt.domain.member.dto.request;

import java.time.LocalDate;

import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.model.constant.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProviderSignUpRequestDto {
	@NotEmpty(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
	
    @NotEmpty(message = "이름은 필수입니다.")
    private String name;

    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String password;

    @NotEmpty(message = "전화번호는 필수입니다.")
    private String phone;
    
    @NotEmpty (message = "성별은 필수입니다.")
    private String gender;

    @Past(message = "생일은 과거의 날짜만 가능합니다.")
    private LocalDate birthDate; 

    @NotEmpty(message = "우편번호는 필수입니다.")
    private String postalCode;
    
    @NotEmpty(message = "주소는 필수입니다.")
    private String address;

    @NotEmpty(message = "상세 주소는 필수입니다.")
    private String detailAddress;
    
    private String businessName;
    private String businessNumber;
    private String bankCode;
    private String account;
    private String accountOwner;
    
    
    public Member toEntity() {
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .name(this.name)
                .phone(this.phone)
                .gender(this.gender)
                .birthDate(this.birthDate)
                .postalCode(this.postalCode)
                .detailAddress(this.detailAddress)
                .address(this.address)
                .businessName(this.businessName)
                .businessNumber(this.businessNumber)
                .bankCode(this.bankCode)
                .account(this.account)
                .accountOwner(this.accountOwner)
                .role(Role.ROLE_PROV)
                .build();
    }
}
