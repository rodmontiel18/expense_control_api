package com.rodmontiel.ec.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rodmontiel.ec.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	@Query(value ="SELECT c FROM Category c WHERE UPPER(c.user.email) = UPPER(?1)")
	public Collection<Category> getCategoriesByUserEmail(String userEmail);
	
	@Query(value = "SELECT c FROM Category c WHERE UPPER(c.user.email) = UPPER(?2) AND UPPER(c.name) = UPPER(?1)")
	public Collection<Category> getCategoryByNameAndUser(String categotyName, String userEmail);
	
	@Query(value = "SELECT c FROM Category c WHERE UPPER(c.user.email) = UPPER(?2) AND c.type = ?1")
	public Collection<Category> getCategoriesByTypeAndUserEmail(int categoryType, String userEmail);
	
	@Query(value = "SELECT c FROM Category c WHERE UPPER(c.user.email) = UPPER(?2) AND c.id = ?1")
	public Optional<Category> getCategoryByUserEmailAndCategoryId(long categoryId, String userEmail);
}
