package com.as.ec.model.ex;

public class ExpenseNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3099033473032473966L;

	public ExpenseNotFoundException(String error) {
		super(error);
	}
}
