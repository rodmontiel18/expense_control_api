package com.as.ec.controller;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.as.ec.model.User;
import com.as.ec.model.dal.UserService;
import com.as.ec.model.ex.RepeatedValueEx;
import com.as.ec.model.ex.UserNotFoundException;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService service;
	
	@GetMapping("/login")
	public ResponseEntity<User> userLogin(@RequestHeader(value = "Authorization") String authData) {

		User user = null;
		String[] arrAuthData = authData.split(" ");
		
		if(arrAuthData.length > 0) {
			
			String strLoginData = new String(Base64.getDecoder().decode(arrAuthData[1]));
			
			String [] arrLoginData = strLoginData.split(":");
			
			if(arrLoginData.length > 0) {
				String email = arrLoginData[0];
				String pwd = arrLoginData[1];
			
				user = service.userLogin(email, pwd);
			}
			
		}
		
		if(user == null)
			throw new UserNotFoundException("Usuario y/o contraseña invalidos");
			
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<User> addUser(@RequestBody User user) {
		
		User lUser = service.searchUserByEmail(user.getEmail());
		
		if(lUser != null)
			throw new RepeatedValueEx("El email ingresado no esta disponible para un registro nuevo");
		
		lUser = service.save(user);
		
		return new ResponseEntity<User>(lUser, HttpStatus.OK);
	}
}
