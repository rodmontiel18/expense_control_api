package com.rodmontiel.ec.dto;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class UserDTO {
	@JsonFormat(shape = Shape.NUMBER)
	public Date birthday;
	// private List<Category> categories;
	public String email;
	// private List<Expense> expenses;
	public char genre;
	public long id;
	// private List<Income> income;
	public String lastname;
	public String name;
	public String password;
	
}
