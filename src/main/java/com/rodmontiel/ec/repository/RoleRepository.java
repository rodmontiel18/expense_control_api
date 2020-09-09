package com.rodmontiel.ec.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rodmontiel.ec.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	@Query(value = "SELECT r from Role r WHERE UPPER(r.name) = UPPER(?1)")
	public Optional<Role> findByName(String name);
}
