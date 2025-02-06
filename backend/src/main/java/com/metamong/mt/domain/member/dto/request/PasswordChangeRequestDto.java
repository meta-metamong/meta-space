package com.metamong.mt.domain.member.dto.request;

import lombok.Getter;

@Getter
public class PasswordChangeRequestDto {
    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirm;
}
