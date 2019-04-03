package com.caowei.entity;

import java.io.Serializable;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority,Serializable {
	
	private static final long serialVersionUID = 1L;

	private long id;
	private String rolename;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	@Override
	public String getAuthority() {
		return rolename;
	}
}
