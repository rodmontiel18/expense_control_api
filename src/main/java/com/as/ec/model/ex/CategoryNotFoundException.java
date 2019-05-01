package com.as.ec.model.ex;

public class CategoryNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5973177171748042676L;

	public CategoryNotFoundException(String error) {
		super(error);
	}
}
