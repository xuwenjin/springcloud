package com.xwj.jwt;

import lombok.AllArgsConstructor;

/**
 * jwt用户信息
 */
@AllArgsConstructor
public class JwtUserInfo implements IJwtInfo {

	private String username;
	private String userId;
	private String name;

	@Override
	public String getUniqueName() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		JwtUserInfo JwtUserInfo = (JwtUserInfo) o;

		if (username != null ? !username.equals(JwtUserInfo.username) : JwtUserInfo.username != null) {
			return false;
		}
		return userId != null ? userId.equals(JwtUserInfo.userId) : JwtUserInfo.userId == null;

	}

	@Override
	public int hashCode() {
		int result = username != null ? username.hashCode() : 0;
		result = 31 * result + (userId != null ? userId.hashCode() : 0);
		return result;
	}

}
