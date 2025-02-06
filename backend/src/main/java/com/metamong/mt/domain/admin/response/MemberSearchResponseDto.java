package com.metamong.mt.domain.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberSearchResponseDto {
    private String email;             
    private String role;              
    private String accountStatus;    
    private int reservationCount;     
    private String createdAt;         
}