package com.metamong.mt.global.auth.userdetails;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class MemberUserDetails extends User {
	private static final long serialVersionUID = 1L;
	private String name;
	
	public MemberUserDetails(String userId, String password, 
			Collection<? extends GrantedAuthority> authorities, String name) {
		super(userId, password, authorities);
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}