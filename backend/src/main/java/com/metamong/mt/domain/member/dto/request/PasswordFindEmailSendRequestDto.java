package com.metamong.mt.domain.member.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class PasswordFindEmailSendRequestDto {
    
    @NotEmpty
    private String email;
}
