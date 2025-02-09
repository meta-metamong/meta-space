package com.metamong.mt.domain.member.model.constant;

public enum Role {
    ROLE_CONS("CONS"),
    ROLE_PROV("PROV"),
    ROLE_ADMN("ADMN");
    
    private final String role;
    
    Role(String role) {
        this.role = role;
    }
    
    public String role() {
        return this.role;
    }
}
