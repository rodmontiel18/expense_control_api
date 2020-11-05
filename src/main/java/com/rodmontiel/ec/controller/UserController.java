package com.rodmontiel.ec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodmontiel.ec.contracts.v1.response.user.OAuth2LoginRs;
import com.rodmontiel.ec.contracts.v1.request.ResetPasswordRq;
import com.rodmontiel.ec.contracts.v1.request.UpdatePasswordRq;
import com.rodmontiel.ec.contracts.v1.response.BaseResponse;
import com.rodmontiel.ec.contracts.v1.response.user.ConfirmAccountRs;
import com.rodmontiel.ec.contracts.v1.response.user.GetProfileRs;
import com.rodmontiel.ec.contracts.v1.response.user.LoginRs;
import com.rodmontiel.ec.contracts.v1.response.user.SignupRs;

import com.rodmontiel.ec.dto.UserDTO;
import com.rodmontiel.ec.ex.GenericExceptionHandler;
import com.rodmontiel.ec.service.UserService;
import com.rodmontiel.ec.service.VerificationTokenService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private VerificationTokenService verificationTokenService; 

	@GetMapping("/user/get-profile")
	public ResponseEntity<GetProfileRs> getProfile(@RequestHeader(value = "Authorization") String authData) 
		throws GenericExceptionHandler, Exception {
			return new ResponseEntity<GetProfileRs>(userService.getProfile(authData), HttpStatus.OK);
	}

	@GetMapping("/signin")
	public ResponseEntity<LoginRs> userLogin(@RequestHeader(value = "Authorization") String authData)
			throws GenericExceptionHandler, Exception {
		return new ResponseEntity<LoginRs>(userService.login(authData), HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<SignupRs> signup(@RequestBody UserDTO user) throws GenericExceptionHandler, Exception {
		return new ResponseEntity<SignupRs>(userService.signup(user), HttpStatus.OK);
	}

	@GetMapping("/signin/oauth/github/{code}")
	public ResponseEntity<OAuth2LoginRs> githubLogin(@PathVariable String code) throws GenericExceptionHandler, Exception {
		return new ResponseEntity<OAuth2LoginRs>(userService.oauth2Login(code), HttpStatus.OK);
	}

	@GetMapping("/confirm-account/{token}")
	public ResponseEntity<ConfirmAccountRs> confirmAccount(@PathVariable String token) throws GenericExceptionHandler, Exception {
		return new ResponseEntity<ConfirmAccountRs>(verificationTokenService.confirmAccount(token), HttpStatus.OK);
	}

	@GetMapping("/request-reset-password/{email}")
	public ResponseEntity<BaseResponse> requestResetPassword(@PathVariable String email) throws GenericExceptionHandler, Exception {
		return new ResponseEntity<BaseResponse>(userService.requestResetPassword(email), HttpStatus.OK);
	}

	@PutMapping(value="/reset-password")
	public ResponseEntity<BaseResponse> resetPassword(@RequestBody ResetPasswordRq request) throws GenericExceptionHandler, Exception {
		return new ResponseEntity<BaseResponse>(verificationTokenService.resetPassword(request), HttpStatus.OK);
	}

	@PutMapping(value= "/user/update-password")
	public ResponseEntity<BaseResponse> updatePasword(@RequestHeader(value= "Authorization") String authData, @RequestBody UpdatePasswordRq request)
		throws GenericExceptionHandler, Exception {
		return new ResponseEntity<BaseResponse>(userService.updatePassword(authData, request), HttpStatus.OK);
	}

	@PutMapping(value="/user/update-profile")
	public ResponseEntity<BaseResponse> updateProfile(@RequestHeader(value = "Authorization") String authData, @RequestBody UserDTO userDto)
		throws GenericExceptionHandler, Exception {
		return new ResponseEntity<BaseResponse>(userService.updateProfile(authData, userDto), HttpStatus.OK);
	}
	

}
