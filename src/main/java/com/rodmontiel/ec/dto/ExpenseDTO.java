package com.rodmontiel.ec.dto;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ExpenseDTO {
	public double amount;
	public long categoryId;
	public String description;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
	public Date expenseDate;
	public long id;
	public long userId;
}
