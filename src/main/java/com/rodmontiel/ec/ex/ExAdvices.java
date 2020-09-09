package com.rodmontiel.ec.ex;

import java.util.ArrayList;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.rodmontiel.ec.contracts.v1.response.BaseResponse;

@ControllerAdvice
public class ExAdvices {
	
	@Autowired
	private Logger gLogger;

	@ResponseBody
	@ExceptionHandler(UserException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public BaseResponse userExceptionHandler(UserException ex) {
		return handleCustomException(ex);
	}
	
	@ResponseBody
	@ExceptionHandler(ExpenseException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public BaseResponse expenseNotFoundHandler(ExpenseException ex) {
		return handleCustomException(ex);
	}
	
	@ResponseBody
	@ExceptionHandler(CategoryException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public BaseResponse categoryNotFoundHandler(CategoryException ex) {
		return handleCustomException(ex);
	}
	
	@ResponseBody
	@ExceptionHandler(IncomeException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public BaseResponse incomeNotFoundHandler(IncomeException ex) {
		return handleCustomException(ex);
	}
	
	@ResponseBody
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public BaseResponse constraintViolationExceptionHandler(Exception ex) {
		ArrayList<String> errors = new ArrayList<String>();
		errors.add("Something wrong happened");
		gLogger.error("-----> Error: " + ex.getMessage() + "\n =====> " + ExceptionUtils.getStackTrace(ex));
		return new BaseResponse(errors, false);
	}
	
	private BaseResponse handleCustomException(Exception ex) {
		ArrayList<String> errors = new ArrayList<>();
		errors.add(ex.getMessage());
		return new BaseResponse(errors, false); 
	}
}
