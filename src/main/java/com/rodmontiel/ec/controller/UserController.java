package com.rodmontiel.ec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodmontiel.ec.contracts.v1.response.user.LoginRs;
import com.rodmontiel.ec.contracts.v1.response.user.SignupRs;
import com.rodmontiel.ec.dto.UserDTO;
import com.rodmontiel.ec.ex.UserException;
import com.rodmontiel.ec.service.UserService;

@RestController
@RequestMapping
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/signin")
	public ResponseEntity<LoginRs> userLogin(@RequestHeader(value = "Authorization") String authData)
			throws UserException, Exception {
		return new ResponseEntity<LoginRs>(userService.login(authData), HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<SignupRs> signup(@RequestBody UserDTO user) throws UserException, Exception {
		return new ResponseEntity<SignupRs>(userService.signup(user), HttpStatus.OK);
	}

}
