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
import org.springframework.web.bind.annotation.RestController;

import com.rodmontiel.ec.contracts.v1.response.expense.AddExpenseRs;
import com.rodmontiel.ec.contracts.v1.response.expense.DeleteExpenseRs;
import com.rodmontiel.ec.contracts.v1.response.expense.DeleteUserExpenseRs;
import com.rodmontiel.ec.contracts.v1.response.expense.EditUserExpenseRs;
import com.rodmontiel.ec.contracts.v1.response.expense.GetAllExpensesRs;
import com.rodmontiel.ec.contracts.v1.response.expense.GetExpenseByIdRs;
import com.rodmontiel.ec.contracts.v1.response.expense.GetExpensesByUserAndRangeDateRs;
import com.rodmontiel.ec.contracts.v1.response.expense.GetExpensesFromRangeRs;
import com.rodmontiel.ec.dto.ExpenseDTO;
import com.rodmontiel.ec.ex.ExpenseException;
import com.rodmontiel.ec.ex.UserException;
import com.rodmontiel.ec.service.ExpenseService;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

	@Autowired
	private ExpenseService expenseService;

	@DeleteMapping("/admin/{expenseId}")
	public ResponseEntity<DeleteExpenseRs> delExpense(@PathVariable long expenseId) throws Exception {
		return new ResponseEntity<DeleteExpenseRs>(expenseService.deleteExpense(expenseId), HttpStatus.OK);
	}

	@GetMapping("/admin")
	public ResponseEntity<GetAllExpensesRs> getAllExpenses() throws ExpenseException, Exception {
		return new ResponseEntity<GetAllExpensesRs>(expenseService.getAllExpenses(), HttpStatus.OK);
	}

	@GetMapping("/admin/{expenseId}")
	public ResponseEntity<GetExpenseByIdRs> getExpenseById(@PathVariable long expenseId)
			throws ExpenseException, Exception {
		return new ResponseEntity<GetExpenseByIdRs>(expenseService.getExpenseById(expenseId), HttpStatus.OK);
	}

	@GetMapping("/admin/range/{from}/{to}")
	public ResponseEntity<GetExpensesFromRangeRs> getExpensesFromRange(@PathVariable long from, @PathVariable long to)
			throws ExpenseException, Exception {
		return new ResponseEntity<GetExpensesFromRangeRs>(expenseService.getExpensesFromRange(from, to), HttpStatus.OK);
	}

	@GetMapping("/user/range/{from}/{to}")
	public ResponseEntity<GetExpensesByUserAndRangeDateRs> getExpensesByUserAndRangeDate(@PathVariable long from,
			@PathVariable long to, @RequestHeader(name = "Authorization") String authData)
			throws ExpenseException, UserException, Exception {
		return new ResponseEntity<GetExpensesByUserAndRangeDateRs>(
				expenseService.getExpensesByUserAndRangeDate(authData, from, to), HttpStatus.OK);
	}

	@GetMapping("/user/{expenseId}")
	public ResponseEntity<GetExpenseByIdRs> getUserExpenseById(@RequestHeader(value = "Authorization") String authData,
			@PathVariable long expenseId) throws ExpenseException, UserException, Exception {
		return new ResponseEntity<GetExpenseByIdRs>(expenseService.getUserExpenseById(authData, expenseId),
				HttpStatus.OK);
	}

	@PutMapping("/user")
	public ResponseEntity<EditUserExpenseRs> editUserExpense(@RequestHeader(value = "Authorization") String authData,
			@RequestBody ExpenseDTO expense) throws ExpenseException, UserException, Exception {
		return new ResponseEntity<EditUserExpenseRs>(expenseService.editUserExpense(authData, expense), HttpStatus.OK);
	}

	@DeleteMapping("/user/{expenseId}")
	public ResponseEntity<DeleteUserExpenseRs> deleteUserExpense(
			@RequestHeader(value = "Authorization") String authData, @PathVariable long expenseId)
			throws ExpenseException, UserException, Exception {
		return new ResponseEntity<DeleteUserExpenseRs>(expenseService.deleteUserExpense(authData, expenseId),
				HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<AddExpenseRs> addExpense(@RequestHeader(value = "Authorization") String authData,
			@RequestBody ExpenseDTO expense) throws ExpenseException, Exception {
		return new ResponseEntity<AddExpenseRs>(expenseService.addExpense(authData, expense), HttpStatus.OK);
	}
}
