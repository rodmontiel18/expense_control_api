package com.rodmontiel.ec.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.rodmontiel.ec.ex.GenericExceptionHandler;
import com.rodmontiel.ec.model.User;
import com.rodmontiel.ec.repository.UserRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository uRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws GenericExceptionHandler {

		User user = uRepository.getUserByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid credendials"));

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				user.isEnabled(), true, true, true, new ArrayList<>());
	}
	
}
