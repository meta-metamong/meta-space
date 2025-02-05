package com.metamong.mt.domain.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequestDto {
	private String from;
    private String text;
    private String userId;
    private String type;
    private String to;
}
