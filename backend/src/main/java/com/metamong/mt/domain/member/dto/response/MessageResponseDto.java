package com.metamong.mt.domain.member.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MessageResponseDto {
    private final String from;
    private final String text;
    private final String userId;
}
