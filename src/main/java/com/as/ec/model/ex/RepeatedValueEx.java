package com.as.ec.model.ex;

public class RepeatedValueEx extends RuntimeException {
	private static final long serialVersionUID = -7256769266411615334L;

	public RepeatedValueEx(String error) {
		super(error);
	}
}
