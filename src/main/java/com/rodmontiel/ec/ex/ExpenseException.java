package com.rodmontiel.ec.ex;

public class ExpenseException extends RuntimeException {

	private static final long serialVersionUID = 3099033473032473966L;

	public ExpenseException(String error) {
		super(error);
	}
}
