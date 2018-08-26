package com.xwj.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String username;

	private String password;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// 账户是否没有过期，默认true
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// 账户是否没有锁定，默认true
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// 密码是否没有过期，默认true
		return true;
	}

	@Override
	public boolean isEnabled() {
		// 账户是否可用，默认true
		return true;
	}

}