package com.metamong.mt.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageResponseDto {
    private String from;
    private String text;
    private String userId;
    private String type;
    
    @Override
    public String toString() {
        return "MessageResponseDto [from=" + from + ", text=" + text + ", userId=" + userId + ", type=" + type + "]";
    }

}
