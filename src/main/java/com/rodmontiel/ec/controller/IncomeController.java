package com.rodmontiel.ec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rodmontiel.ec.contracts.v1.response.income.AddIncomeRs;
import com.rodmontiel.ec.contracts.v1.response.income.DeleteIncomeRs;
import com.rodmontiel.ec.contracts.v1.response.income.EditIncomeRs;
import com.rodmontiel.ec.contracts.v1.response.income.GetIncomeRs;
import com.rodmontiel.ec.contracts.v1.response.income.GetIncomesRs;
import com.rodmontiel.ec.dto.IncomeDTO;
import com.rodmontiel.ec.ex.GenericExceptionHandler;
import com.rodmontiel.ec.service.IncomeService;

@RestController
@RequestMapping("/incomes")
public class IncomeController {

	@Autowired
	private IncomeService iService;

	@PostMapping
	public ResponseEntity<AddIncomeRs> addIncome(@RequestHeader(value = "Authorization") String authData,
			@RequestBody IncomeDTO income) throws GenericExceptionHandler, Exception {
		return new ResponseEntity<AddIncomeRs>(iService.addIncome(authData, income), HttpStatus.OK);
	}

	@GetMapping("/user/{incomeId}")
	public ResponseEntity<GetIncomeRs> getUserIncomeById(@RequestHeader(value = "Authorization") String authData,
			@PathVariable long incomeId) throws GenericExceptionHandler, Exception {
		return new ResponseEntity<GetIncomeRs>(iService.getUserIncomeById(authData, incomeId), HttpStatus.OK);
	}

	@DeleteMapping("/user/{incomeId}")
	public ResponseEntity<DeleteIncomeRs> deleteUserIncome(@RequestHeader(value = "Authorization") String authData,
			@PathVariable long incomeId) throws GenericExceptionHandler, Exception {
		return new ResponseEntity<DeleteIncomeRs>(iService.deleteUserIncome(authData, incomeId), HttpStatus.OK);
	}

	@PutMapping("/user")
	public ResponseEntity<EditIncomeRs> editUserIncome(@RequestBody IncomeDTO dtoIncome,
			@RequestHeader(value = "Authorization") String authData) throws GenericExceptionHandler, Exception {
		return new ResponseEntity<EditIncomeRs>(iService.editUserIncome(authData, dtoIncome), HttpStatus.OK);
	}

	@GetMapping("/user/range")
	public ResponseEntity<GetIncomesRs> getUserIncomesByRangeDate(
			@RequestHeader(value = "Authorization") String authData, @RequestParam long from, @RequestParam long to)
			throws GenericExceptionHandler, Exception {
		return new ResponseEntity<GetIncomesRs>(iService.getUserIncomesByRangeDate(authData, from, to), HttpStatus.OK);
	}

}
