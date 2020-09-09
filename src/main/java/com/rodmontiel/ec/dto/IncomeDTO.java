package com.rodmontiel.ec.dto;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class IncomeDTO {
	
	public double amount;
	public long categoryId;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
	public Date incomeDate;
	public String description;
	public long id;
	public long userId;

}
