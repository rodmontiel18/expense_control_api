package com.rodmontiel.ec.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rodmontiel.ec.model.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "SELECT u FROM User u WHERE UPPER(u.email) = UPPER(?1) and u.password = ?2")
	public Optional<User> userLogin(String username, String password);
	
	@Query(value = "SELECT u FROM User u WHERE UPPER(u.email) = UPPER(?1)")
	public Optional<User> getUserByEmail(String email);
}
