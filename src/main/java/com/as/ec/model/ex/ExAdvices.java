package com.as.ec.model.ex;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExAdvices {

	@ResponseBody
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorObject userNotFoundHandler(UserNotFoundException ex) {
		ErrorObject eObj = new ErrorObject(200, ex.getMessage());
		return eObj;
	}
	
	@ResponseBody
	@ExceptionHandler(ExpenseNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorObject expenseNotFoundHandler(ExpenseNotFoundException ex) {
		ErrorObject eObj = new ErrorObject(200, ex.getMessage());
		return eObj;
	}
	
	@ResponseBody
	@ExceptionHandler(CategoryNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorObject categoryNotFoundHandler(CategoryNotFoundException ex) {
		ErrorObject eObj = new ErrorObject(200, ex.getMessage());
		return eObj;
	}
	
	@ResponseBody
	@ExceptionHandler(IncomeNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorObject incomeNotFoundHandler(IncomeNotFoundException ex) {
		ErrorObject eObj = new ErrorObject(200, ex.getMessage());
		return eObj;
	}
	
	@ResponseBody
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorObject constraintViolationExceptionHandler(DataIntegrityViolationException ex) {
		ErrorObject eObj = new ErrorObject(250, "No puedes borrar esta categoria, ya que tienes gastos asociados a ella");
		return eObj;
	}
	
	@ResponseBody
	@ExceptionHandler(RepeatedValueEx.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorObject repeatedValueExHandler(RepeatedValueEx ex) {
		ErrorObject eObj = new ErrorObject(250, ex.getMessage());
		return eObj;
	}
	
	@ResponseBody
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorObject constraintViolationExceptionHandler(Exception ex) {
		ErrorObject eObj = new ErrorObject(290, "Ha ocurrido un error inesperado");
		return eObj;
	}
}
