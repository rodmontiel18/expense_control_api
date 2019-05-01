package com.as.ec.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.as.ec.model.Income;
import com.as.ec.model.dal.IncomeService;
import com.as.ec.model.dal.UserService;
import com.as.ec.model.ex.IncomeNotFoundException;

@RestController
@RequestMapping("/income")
public class IncomeController {
	
	private IncomeService iService;
	private UserService uService;
	
	@Autowired
	public IncomeController(IncomeService incomeService, UserService userService) {
		this.iService = incomeService;
		this.uService = userService;
	}
	
	@PostMapping
	public ResponseEntity<Income> addIncome(@RequestBody Income income){
		Income lIncome = iService.save(income);
		return new ResponseEntity<Income>(lIncome, HttpStatus.OK);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteIncome(@PathVariable long id){
		iService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("{id}")
	public ResponseEntity<Income> editIncome(@RequestBody Income income, @PathVariable long id) {
		
		Income updatedIncome = iService.findById(id)
								.map(lIncome -> {
									lIncome.setAmount(income.getAmount());
									lIncome.setDescription(income.getDescription());
									lIncome.setId(income.getId());
									lIncome.setIncomeDate(income.getIncomeDate());
									lIncome.setUser(
											uService.findById(income.getUser().getId())
												.orElseThrow(() -> new IncomeNotFoundException("No se encontro el usuario asociado al ingreso con id " + id))
									);
									return iService.save(lIncome);
								})
								.orElseGet(() -> {
									income.setId(id);
									return iService.save(income);
								});
								
		
		return new ResponseEntity<Income>(updatedIncome, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Income> getIncomeById(@PathVariable long id) {
		Income income = iService.findById(id)
							.orElseThrow(() -> 
								new IncomeNotFoundException("No se encontro el ingreso con el id especificado")
							);
		
		return new ResponseEntity<Income>(income, HttpStatus.OK);
	}

	@GetMapping("user/{userId}/range/{from}/{to}")
	public ResponseEntity<List<Income>> getUserIncome(@PathVariable long userId, @PathVariable long from, @PathVariable long to){
		Date lFrom = new Date(from);
		Date lTo = new Date(to);
		List<Income> income = iService.getUserIncomeByRangeDate(userId, lFrom, lTo);
		return new ResponseEntity<List<Income>>(income, HttpStatus.OK);
	}
}
