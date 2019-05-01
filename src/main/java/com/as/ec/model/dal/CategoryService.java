package com.as.ec.model.dal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.as.ec.model.Category;

public interface CategoryService extends JpaRepository<Category, Long> {

	@Query(value ="SELECT c FROM Category c WHERE c.user.id = ?1")
	public List<Category> getCategoriesByUser(long userId);
	
	@Query(value = "SELECT * FROM category c WHERE c.user_id = ?1 AND UPPER(c.name) = UPPER(?2) LIMIT 1", nativeQuery = true)
	public Category searchCategoriesByUserAndName(long userId, String name);
}
