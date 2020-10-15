package com.rodmontiel.ec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodmontiel.ec.contracts.v1.response.income.AddIncomeRs;
import com.rodmontiel.ec.contracts.v1.response.income.DeleteIncomeRs;
import com.rodmontiel.ec.contracts.v1.response.income.EditIncomeRs;
import com.rodmontiel.ec.contracts.v1.response.income.GetIncomeRs;
import com.rodmontiel.ec.contracts.v1.response.income.GetIncomesRs;
import com.rodmontiel.ec.dto.IncomeDTO;
import com.rodmontiel.ec.ex.IncomeException;
import com.rodmontiel.ec.ex.UserException;
import com.rodmontiel.ec.service.IncomeService;

@CrossOrigin(origins = {
	"http://rodmontiel.com", "http://www.rodmontiel.com",
	"https://rodmontiel.com", "https://www.rodmontiel.com"
}, maxAge = 3600)
@RestController
@RequestMapping("/incomes")
public class IncomeController {

	@Autowired
	private IncomeService iService;
	
	@GetMapping("/admin")
	public ResponseEntity<GetIncomesRs> getAllIncomes() throws Exception {
		return new ResponseEntity<GetIncomesRs>(iService.getAllIncomes(), HttpStatus.OK);
	}

	@GetMapping("/admin/{incomeId}")
	public ResponseEntity<GetIncomeRs> getIncomeById(@PathVariable long incomeId) {
		return new ResponseEntity<GetIncomeRs>(iService.getIncomeById(incomeId), HttpStatus.OK);
	}

	@DeleteMapping("/admin/{incomeId}")
	public ResponseEntity<DeleteIncomeRs> deleteIncome(@PathVariable long incomeId) throws Exception {
		return new ResponseEntity<DeleteIncomeRs>(iService.deleteIncome(incomeId), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<AddIncomeRs> addIncome(@RequestHeader(value = "Authorization") String authData,
			@RequestBody IncomeDTO income) throws UserException, Exception {
		return new ResponseEntity<AddIncomeRs>(iService.addIncome(authData, income), HttpStatus.OK);
	}
	
	@GetMapping("/user/{incomeId}")
	public ResponseEntity<GetIncomeRs> getUserIncomeById(@RequestHeader(value = "Authorization") String authData,
			@PathVariable long incomeId) throws IncomeException, UserException, Exception {
		return new ResponseEntity<GetIncomeRs>(iService.getUserIncomeById(authData, incomeId), HttpStatus.OK);
	}
	

	@DeleteMapping("/user/{incomeId}")
	public ResponseEntity<DeleteIncomeRs> deleteUserIncome(@RequestHeader(value = "Authorization") String authData,
			@PathVariable long incomeId) throws UserException, IncomeException, Exception {
		return new ResponseEntity<DeleteIncomeRs>(iService.deleteUserIncome(authData, incomeId), HttpStatus.OK);
	}

	@PutMapping("/user")
	public ResponseEntity<EditIncomeRs> editUserIncome(@RequestBody IncomeDTO dtoIncome,
			@RequestHeader(value = "Authorization") String authData) throws IncomeException, UserException, Exception {
		return new ResponseEntity<EditIncomeRs>(iService.editUserIncome(authData, dtoIncome), HttpStatus.OK);
	}

	@GetMapping("/user/range/{from}/{to}")
	public ResponseEntity<GetIncomesRs> getUserIncomesByRangeDate(
			@RequestHeader(value = "Authorization") String authData, @PathVariable long from, @PathVariable long to)
			throws IncomeException, UserException, Exception {
		return new ResponseEntity<GetIncomesRs>(iService.getUserIncomesByRangeDate(authData, from, to),
				HttpStatus.OK);
	}
	
}
