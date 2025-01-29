package com.metamong.mt.domain.member.dto.response;

import com.metamong.mt.domain.member.model.Role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class LoginInfoResponseDto {
    private final String userId;
    private final String name;
    private final String password;
    private final Role role;
}
