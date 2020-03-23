package com.xwj.service;

public interface AuthService {

	String login(String username, String password);

	String refresh(String oldToken);

}
