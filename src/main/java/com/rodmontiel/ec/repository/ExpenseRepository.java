package com.rodmontiel.ec.repository;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rodmontiel.ec.model.Expense;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense, Long> {

	@Query(value = "SELECT e FROM Expense e WHERE e.expenseDate BETWEEN ?1 AND ?2")
	public Collection<Expense> getExpensesFromRange(Date from, Date to);
	
	@Query(value = "SELECT e FROM Expense e WHERE UPPER(user.email) = UPPER(?1) AND e.expenseDate BETWEEN ?2 AND ?3 ORDER BY e.expenseDate ASC")
	public Collection<Expense> getExpensesByUserAndRange(String email, Date from, Date to);
	
	@Query(value = "SELECT e FROM Expense e WHERE e.id = ?1 AND UPPER(e.user.email) = UPPER(?2)")
	public Optional<Expense> getExpenseByIdAndUserEmail(long expenseId, String userEmail);
	
	@Query(value = "SELECT e FROM Expense e WHERE e.category.id = ?1 AND UPPER(e.user.email) = UPPER(?2)")
	public Collection<Expense> getUserExpensesByCategory(long categoryId, String userEmail);
}
