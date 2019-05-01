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

import com.as.ec.model.Expense;
import com.as.ec.model.dal.CategoryService;
import com.as.ec.model.dal.ExpenseService;
import com.as.ec.model.dal.UserService;
import com.as.ec.model.ex.ExpenseNotFoundException;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

	private CategoryService categoryService;
	private ExpenseService expenseService;
	private UserService userService;
	
	@Autowired
	public ExpenseController(CategoryService cService, ExpenseService eService, UserService uService) {
		categoryService = cService;
		expenseService = eService;
		userService = uService;
	}
	
	
	@GetMapping
	public ResponseEntity<List<Expense>> getAll() {
		
		List<Expense> expenseList = expenseService.findAll();
		
		return new ResponseEntity<List<Expense>>(expenseList, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Expense> getById(@PathVariable long id){
		Expense expense = expenseService.findById(id)
							.orElseThrow(() -> new ExpenseNotFoundException("No se encontro el gasto con el id: " + id));
		return new ResponseEntity<Expense>(expense, HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}/range/{from}/{to}")
	public ResponseEntity<List<Expense>> getUserAndRangeExpenseList(@PathVariable long userId, @PathVariable long from, @PathVariable long to) {
		Date lFrom = new Date(from);
		Date lTo = new Date(to);
		List<Expense> expenses = expenseService.getUserAndRangeExpenseList(userId, lFrom, lTo);
		return new ResponseEntity<List<Expense>>(expenses, HttpStatus.OK);
	}
	
	@GetMapping("{from}/{to}")
	public ResponseEntity<List<Expense>> getRange(@PathVariable long from, @PathVariable long to) {
		Date lFrom = new Date(from);
		Date lTo = new Date(to);
		List<Expense> expenses = expenseService.getExpensesFromRange(lFrom, lTo);
		return new ResponseEntity<List<Expense>>(expenses, HttpStatus.OK);
	}
	
	@PutMapping("{id}")
	public ResponseEntity<Expense> editExpense(@RequestBody Expense expense, @PathVariable long id){
	
		Expense updatedExpense = expenseService.findById(id)
				.map(lExpense -> {
					lExpense.setAmount(expense.getAmount());
					lExpense.setDescription(expense.getDescription());
					lExpense.setExpenseDate(expense.getExpenseDate());
					lExpense.setId(expense.getId());
					lExpense.setCategory(
							categoryService.findById(expense.getCategory().getId())
								.orElseThrow(() -> new ExpenseNotFoundException("No se encontro la categoria asociada"))
							);
					lExpense.setUser(
							userService.findById(expense.getUser().getId())
								.orElseThrow(() -> new ExpenseNotFoundException("No se encontro el usuario asociado"))
							);
					return expenseService.save(lExpense);
				})
				.orElseGet(() -> {
					expense.setId(id);
					return expenseService.save(expense);
				});
		return new ResponseEntity<Expense>(updatedExpense, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Expense> addExpense(@RequestBody Expense expense){
		Expense lExpense = expenseService.save(expense);
		if(lExpense == null)
			return new ResponseEntity<Expense>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<Expense>(lExpense, HttpStatus.OK);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> delExpense(@PathVariable long id){
		expenseService.deleteById(id);
		
		return ResponseEntity.noContent().build();
	}
}
