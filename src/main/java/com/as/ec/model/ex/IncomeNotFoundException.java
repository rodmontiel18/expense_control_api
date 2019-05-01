package com.as.ec.model.ex;

public class IncomeNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -5139613204825048225L;

	public IncomeNotFoundException(String error) {
		super(error);
	}
}
