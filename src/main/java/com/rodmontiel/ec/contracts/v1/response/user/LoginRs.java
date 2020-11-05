package com.rodmontiel.ec.contracts.v1.response.user;

import java.sql.Date;

import com.rodmontiel.ec.contracts.v1.response.BaseResponse;

public class LoginRs extends BaseResponse {

	public String email;
	public String token;
	public String name;
	public Date birthday;

}
