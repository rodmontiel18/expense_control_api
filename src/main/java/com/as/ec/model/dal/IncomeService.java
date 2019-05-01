package com.as.ec.model.dal;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.as.ec.model.Income;

public interface IncomeService extends JpaRepository<Income, Long> {

	@Query(value = "SELECT i FROM Income i WHERE i.user.id = ?1 AND i.incomeDate BETWEEN ?2 AND ?3 ORDER BY i.incomeDate ASC")
	public List<Income> getUserIncomeByRangeDate(long userId, Date from, Date to);
}
