package org.halkneistiyor.datamodel;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

	USER(0),

	ADMIN(1),

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
