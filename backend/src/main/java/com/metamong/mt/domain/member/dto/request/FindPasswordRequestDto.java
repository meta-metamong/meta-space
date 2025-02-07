package com.metamong.mt.domain.member.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FindPasswordRequestDto {
    
    @NotEmpty(message="이메일은 필수 입력 값입니다.")
    private String email;
}
