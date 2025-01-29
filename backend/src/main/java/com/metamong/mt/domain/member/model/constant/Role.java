package com.metamong.mt.domain.member.model.constant;

public enum Role {
    ROLE_USER,
    ROLE_OWNER,
    ROLE_ADMIN;
    
    private final String role;
    
    Role() {
        this.role = name().substring("ROLE_".length());
    }
    
    public String role() {
        return this.role;
    }
}
