package com.rodmontiel.ec.ex;

public class UserException extends RuntimeException {
	private static final long serialVersionUID = -6437157094962318456L;

	public UserException(String error) {
		super(error);
	}
}
