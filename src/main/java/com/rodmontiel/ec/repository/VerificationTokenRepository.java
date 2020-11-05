package com.rodmontiel.ec.repository;

import java.util.Optional;

import com.rodmontiel.ec.model.VerificationToken;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {
  @Query(value = "SELECT t FROM VerificationToken t WHERE t.token = ?1")
  Optional<VerificationToken> findByConfirmationToken(String confirmationToken);
}
