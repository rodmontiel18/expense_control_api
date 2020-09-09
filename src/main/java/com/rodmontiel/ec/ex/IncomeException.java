package com.rodmontiel.ec.ex;

public class IncomeException extends RuntimeException {
	
	private static final long serialVersionUID = -5139613204825048225L;

	public IncomeException(String error) {
		super(error);
	}
}
