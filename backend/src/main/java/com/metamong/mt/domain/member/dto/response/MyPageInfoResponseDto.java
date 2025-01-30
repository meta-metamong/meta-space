package com.metamong.mt.domain.member.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class MyPageInfoResponseDto {
    private final String userId;
    private final String name;
    private final String email;
    private final String address;
}
