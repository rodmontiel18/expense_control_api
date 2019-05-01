package com.as.ec.model.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.as.ec.model.User;

@Service
public interface UserService extends JpaRepository<User, Long> {

	@Query(value = "SELECT u FROM User u WHERE u.email = ?1 and u.password = ?2")
	public User userLogin(String email, String password);
	
	@Query(value = "SELECT u FROM User u WHERE u.email = ?1")
	public User searchUserByEmail(String email);
}
