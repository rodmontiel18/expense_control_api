package com.rodmontiel.ec.contracts.v1.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {

	public int code;
	public String error;
	public boolean success;
}
