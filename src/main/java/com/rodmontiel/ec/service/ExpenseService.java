package com.rodmontiel.ec.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodmontiel.ec.contracts.v1.response.expense.AddExpenseRs;
import com.rodmontiel.ec.contracts.v1.response.expense.DeleteExpenseRs;
import com.rodmontiel.ec.contracts.v1.response.expense.DeleteUserExpenseRs;
import com.rodmontiel.ec.contracts.v1.response.expense.EditUserExpenseRs;
import com.rodmontiel.ec.contracts.v1.response.expense.GetAllExpensesRs;
import com.rodmontiel.ec.contracts.v1.response.expense.GetExpenseByIdRs;
import com.rodmontiel.ec.contracts.v1.response.expense.GetExpensesByUserAndRangeDateRs;
import com.rodmontiel.ec.contracts.v1.response.expense.GetExpensesFromRangeRs;
import com.rodmontiel.ec.dto.ExpenseDTO;
import com.rodmontiel.ec.ex.GenericExceptionHandler;
import com.rodmontiel.ec.model.Category;
import com.rodmontiel.ec.model.Expense;
import com.rodmontiel.ec.model.User;
import com.rodmontiel.ec.repository.CategoryRepository;
import com.rodmontiel.ec.repository.ExpenseRepository;
import com.rodmontiel.ec.repository.UserRepository;
import com.rodmontiel.ec.security.JwtToken;

@Service
public class ExpenseService {

	@Autowired
	private CategoryRepository categoryRep;
	@Autowired
	private ExpenseRepository expenseRep;
	@Autowired
	private Logger gLogger;
	@Autowired
	private JwtToken tokenTools;
	@Autowired
	private UserRepository userRep;

	public EditUserExpenseRs editUserExpense(String authData, ExpenseDTO expenseDto)
			throws GenericExceptionHandler, Exception {

		EditUserExpenseRs rs = new EditUserExpenseRs();

		String userEmail = tokenTools.getUsernameFromAuthorization(authData);

		Expense expenseToUpdate = expenseRep.getExpenseByIdAndUserEmail(expenseDto.id, userEmail)
				.orElseThrow(() -> new GenericExceptionHandler(311));

		User user = userRep.getUserByEmail(userEmail).orElseThrow(() -> new GenericExceptionHandler(130));

		Category category = categoryRep.getCategoryByUserEmailAndCategoryId(expenseDto.categoryId, userEmail)
				.orElseThrow(() -> new GenericExceptionHandler(312));

		expenseToUpdate.setAmount(expenseDto.amount);
		expenseToUpdate.setCategory(category);
		expenseToUpdate.setDescription(expenseDto.description);
		expenseToUpdate.setExpenseDate(expenseDto.expenseDate);
		expenseToUpdate.setUser(user);

		expenseToUpdate = expenseRep.save(expenseToUpdate);

		rs.expense = mapExpenseToDto(expenseToUpdate);
		rs.success = true;

		return rs;
	}

	public GetExpenseByIdRs getExpenseById(long expenseId) throws GenericExceptionHandler, Exception {

		GetExpenseByIdRs rs = new GetExpenseByIdRs();
		Expense expense = expenseRep.findById(expenseId).orElseThrow(() -> new GenericExceptionHandler(313));
		rs.expense = mapExpenseToDto(expense);
		rs.success = true;

		return rs;
	}

	public GetAllExpensesRs getAllExpenses() throws Exception {

		GetAllExpensesRs rs = new GetAllExpensesRs();

		ArrayList<Expense> expenses = (ArrayList<Expense>) expenseRep.findAll();

		rs.expenses = mapExpensesToDto(expenses);
		rs.success = true;

		return rs;
	}

	public GetExpensesFromRangeRs getExpensesFromRange(long from, long to) throws GenericExceptionHandler, Exception {

		GetExpensesFromRangeRs rs = new GetExpensesFromRangeRs();

		Timestamp lFrom;
		Timestamp lTo;

		try {
			lFrom = new Timestamp(from);
			lTo = new Timestamp(to);
		} catch (Exception e) {
			throw new GenericExceptionHandler(301);
		}

		ArrayList<Expense> expenses = (ArrayList<Expense>) expenseRep.getExpensesFromRange(lFrom, lTo);

		rs.expenses = mapExpensesToDto(expenses);
		rs.success = true;

		return rs;
	}

	public GetExpensesByUserAndRangeDateRs getExpensesByUserAndRangeDate(String authData, long from, long to)
			throws GenericExceptionHandler, Exception {

		GetExpensesByUserAndRangeDateRs rs = new GetExpensesByUserAndRangeDateRs();

		String userEmail = tokenTools.getUsernameFromAuthorization(authData);

		Timestamp lFrom;
		Timestamp lTo;
		try {
			lFrom = new Timestamp(from);
			lTo = new Timestamp(to);
		} catch (Exception e) {
			throw new GenericExceptionHandler(301);
		}

		ArrayList<Expense> expenses = (ArrayList<Expense>) expenseRep.getExpensesByUserAndRange(userEmail, lFrom, lTo);

		rs.expenses = mapExpensesToDto(expenses);
		rs.success = true;

		return rs;

	}

	public GetExpenseByIdRs getUserExpenseById(String authData, long expenseId)
			throws GenericExceptionHandler, Exception {
		GetExpenseByIdRs rs = new GetExpenseByIdRs();

		String userEmail = tokenTools.getUsernameFromAuthorization(authData);

		Expense expense = expenseRep.getExpenseByIdAndUserEmail(expenseId, userEmail)
				.orElseThrow(() -> new GenericExceptionHandler(313));

		rs.expense = mapExpenseToDto(expense);
		rs.success = true;

		return rs;
	}

	public AddExpenseRs addExpense(String authData, ExpenseDTO expense) throws GenericExceptionHandler, Exception {

		try {

			AddExpenseRs rs = new AddExpenseRs();

			String userEmail = tokenTools.getUsernameFromAuthorization(authData);
			User user = userRep.getUserByEmail(userEmail).orElseThrow(() -> new GenericExceptionHandler(130));

			Expense expenseToSave = mapDtoToExpense(expense);
			expenseToSave.setUser(user);

			rs.expense = mapExpenseToDto(expenseRep.save(expenseToSave));
			rs.success = true;

			return rs;

		} catch (Exception ex) {
			gLogger.error("-----> Error: " + ex.getMessage() + "\n =====> " + ExceptionUtils.getStackTrace(ex));
			throw new GenericExceptionHandler(314);
		}

	}

	public DeleteExpenseRs deleteExpense(long expenseId) {
		DeleteExpenseRs rs = new DeleteExpenseRs();
		expenseRep.deleteById(expenseId);
		rs.success = true;
		return rs;
	}

	public DeleteUserExpenseRs deleteUserExpense(String authData, long expenseId)
			throws GenericExceptionHandler, Exception {

		DeleteUserExpenseRs rs = new DeleteUserExpenseRs();

		String userEmail = tokenTools.getUsernameFromAuthorization(authData);

		expenseRep.getExpenseByIdAndUserEmail(expenseId, userEmail).orElseThrow(() -> new GenericExceptionHandler(315));

		expenseRep.deleteById(expenseId);

		rs.success = true;

		return rs;

	}

	private Expense mapDtoToExpense(ExpenseDTO dtoExpense) {
		Expense expense = new Expense();
		expense.setAmount(dtoExpense.amount);

		Category cat = new Category();
		cat.setId(dtoExpense.categoryId);

		expense.setCategory(cat);
		expense.setDescription(dtoExpense.description);
		expense.setExpenseDate(dtoExpense.expenseDate);
		expense.setId(dtoExpense.id);
		return expense;
	}

	private ExpenseDTO mapExpenseToDto(Expense expense) {
		ExpenseDTO dtoExpense = new ExpenseDTO();

		dtoExpense.amount = expense.getAmount();
		dtoExpense.categoryId = expense.getCategory().getId();
		dtoExpense.description = expense.getDescription();
		dtoExpense.expenseDate = expense.getExpenseDate();
		dtoExpense.id = expense.getId();
		dtoExpense.userId = expense.getUser().getId();

		return dtoExpense;
	}

	private Collection<ExpenseDTO> mapExpensesToDto(Collection<Expense> expenses) {
		Collection<ExpenseDTO> dtoExpenses = new ArrayList<ExpenseDTO>();
		for (Expense expense : expenses) {
			dtoExpenses.add(mapExpenseToDto(expense));
		}
		return dtoExpenses;
	}
}
