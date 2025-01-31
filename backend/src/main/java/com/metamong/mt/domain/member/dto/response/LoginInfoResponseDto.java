package com.metamong.mt.domain.member.dto.response;

import com.metamong.mt.domain.member.model.constant.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class LoginInfoResponseDto {
    private final String userId;
    private final String name;
    private final Role role;
}
