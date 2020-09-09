package com.rodmontiel.ec.repository;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rodmontiel.ec.model.Income;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

	@Query(value = "SELECT i FROM Income i WHERE UPPER(i.user.email) = UPPER(?1) AND i.incomeDate BETWEEN ?2 AND ?3 ORDER BY i.incomeDate ASC")
	public Collection<Income> getUserIncomesByRangeDate(String userEmail, Date from, Date to);
	
	@Query(value = "SELECT i FROM Income i WHERE i.id = ?1 AND UPPER(i.user.email) = UPPER(?2)")
	public Optional<Income> getIncomeByIdAndUserEmail(long incomeId, String userEmail);
	
	@Query(value = "SELECT i FROM Income i WHERE i.category.id = ?1 AND UPPER(i.user.email) = UPPER(?2)")
	public Collection<Income> getUserIncomesByCategory(long categoryId, String userEmail);
}
