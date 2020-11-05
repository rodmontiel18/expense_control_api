package com.rodmontiel.ec.ex;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.rodmontiel.ec.config.ResponseCodes;
import com.rodmontiel.ec.contracts.v1.response.BaseResponse;

@ControllerAdvice
public class ExAdvices {
	
	@Autowired
	private Logger gLogger;
	@Autowired
	private ResponseCodes responseCodes;
	
	@ResponseBody
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public BaseResponse unhandledException(Exception ex) {
		gLogger.error("-----> Error: " + ex.getMessage() + "\n =====> " + ExceptionUtils.getStackTrace(ex));
		return new BaseResponse(500, "Something wrong happened", false);
	}

	@ResponseBody
	@ExceptionHandler(GenericExceptionHandler.class)
	@ResponseStatus(HttpStatus.OK)
	public BaseResponse handleCustomException(GenericExceptionHandler ex) {
		return new BaseResponse(ex.code, responseCodes.getErrorDescription(ex.code), false);
	}
}
