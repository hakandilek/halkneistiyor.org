package org.halkneistiyor.security.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

	NEW_USER(0),

	USER(1),

	ADMIN(2),

	;

	private final int bit;

	UserRole(int bit) {
		this.bit = bit;
	}

	public String getAuthority() {
		return toString();
	}

	public int getBit() {
		return bit;
	}

}
