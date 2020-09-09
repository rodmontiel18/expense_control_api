package com.rodmontiel.ec.contracts.v1.response;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {

	public Collection<String> errors;
	public boolean success;
}
