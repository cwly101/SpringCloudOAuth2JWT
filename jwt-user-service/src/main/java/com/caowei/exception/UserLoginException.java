package com.caowei.exception;

@SuppressWarnings("serial")
public class UserLoginException extends RuntimeException {

	public UserLoginException(String message) {
		super(message);
	}
}
