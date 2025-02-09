package com.metamong.mt.domain.member.dto.request;

import com.metamong.mt.domain.member.dto.request.validation.EnumValidator;
import com.metamong.mt.domain.member.model.Account;
import com.metamong.mt.domain.member.model.constant.Gender;
import com.metamong.mt.global.constant.BooleanAlt;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ProviderSignupAccountRequestDto {
    
    @NotEmpty(message = "은행코드는 필수입니다.")
    private String bankCode;
    
    @NotEmpty(message = "계좌번호는 필수입니다.")
    private String accountNumber;
    
    @NotEmpty(message = "동의 여부는 필수입니다.")
    @EnumValidator(enumClass=BooleanAlt.class, message="값은 'Y' 또는 'N'만 가능합니다.")
    private String isAgreedInfo;
    
    public Account toEntity() {
        return Account.builder()
                      .bankCode(this.bankCode)
                      .accountNumber(this.accountNumber)
                      .isAgreedInfo(BooleanAlt.valueOf(isAgreedInfo))
                      .balance((long) 0)
                      .build();
    }
}
