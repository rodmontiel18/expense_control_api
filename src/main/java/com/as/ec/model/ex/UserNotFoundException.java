package com.as.ec.model.ex;

public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -6437157094962318456L;

	public UserNotFoundException(String error) {
		super(error);
	}
}
