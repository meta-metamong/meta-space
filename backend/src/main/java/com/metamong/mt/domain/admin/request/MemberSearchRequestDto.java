package com.metamong.mt.domain.admin.request;

import lombok.Data;

@Data
public class MemberSearchRequestDto {
    private String email;   
    private String role;    
    private String accountStatus; 
}