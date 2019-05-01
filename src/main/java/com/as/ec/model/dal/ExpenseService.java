package com.as.ec.model.dal;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.as.ec.model.Expense;

@Service
public interface ExpenseService extends JpaRepository<Expense, Long> {

	@Query(value = "SELECT e FROM Expense e WHERE e.expenseDate BETWEEN ?1 AND ?2")
	public List<Expense> getExpensesFromRange(Date from, Date to);
	
	@Query(value = "SELECT e FROM Expense e WHERE user.id = ?1 AND e.expenseDate BETWEEN ?2 AND ?3 ORDER BY e.expenseDate ASC")
	public List<Expense> getUserAndRangeExpenseList(long userId, Date from, Date to);
}
