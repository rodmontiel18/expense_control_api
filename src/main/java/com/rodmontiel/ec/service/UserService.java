package com.rodmontiel.ec.service;

import java.util.ArrayList;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rodmontiel.ec.contracts.v1.response.user.LoginRs;
import com.rodmontiel.ec.contracts.v1.response.user.SignupRs;
import com.rodmontiel.ec.dto.UserDTO;
import com.rodmontiel.ec.ex.UserException;
import com.rodmontiel.ec.model.Role;
import com.rodmontiel.ec.model.User;
import com.rodmontiel.ec.repository.RoleRepository;
import com.rodmontiel.ec.repository.UserRepository;
import com.rodmontiel.ec.security.JwtToken;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtToken jwtToken;
	@Autowired
	private BCryptPasswordEncoder pwdEncoder;
	@Autowired
	private UserDetailsService udService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	public LoginRs login(String authData) throws UserException, Exception {

		LoginRs rs = new LoginRs();

		String[] arrAuthData = authData.split(" ");

		if (arrAuthData.length <= 0)
			throw new UserException("Invalid credentials");

		String strLoginData = new String(Base64.getDecoder().decode(arrAuthData[1]));
		String[] arrLoginData = strLoginData.split(":");

		if (arrLoginData.length <= 0)
			throw new UserException("Invalid credentials");

		String email = arrLoginData[0];
		String pwd = arrLoginData[1];

		authenticate(email, pwd);

		final UserDetails userDetails = udService.loadUserByUsername(email);
		User user = userRepository.getUserByEmail(email)
				.orElseThrow(() -> new UserException("The required user does not exixts"));

		String token = jwtToken.generateToken(userDetails);

		rs.email = email;
		rs.name = user.getName();
		rs.token = token;
		rs.success = true;

		return rs;

	}

	public SignupRs signup(UserDTO dtoUser) throws UserException, Exception {
		
		SignupRs rs = new SignupRs();
		
		User user = userRepository.getUserByEmail(dtoUser.email)
				.orElse(null);

		if (user != null)
			throw new UserException("The email you want to use is not available");
		
		User userToSave = mapDtoToUser(dtoUser);
		userToSave.setEnabled(true);
		userToSave.setTokenExpired(true);
		
		Role role = roleRepository.findByName("user")
				.orElseThrow(() -> new Exception("Something wrong happened during register user"));
		
		ArrayList<Role> roles = new ArrayList<Role>();
		roles.add(role);
		
		userToSave.setRoles(roles);
		
		userRepository.save(userToSave);
		
		rs.success = true;
		rs.signupMessage = "User registered successfully";
		
		return rs;
		
	}
	
	public User searchUserByEmail(String email) throws UserException {
		User user = userRepository.getUserByEmail(email).orElse(null);
		if (user == null)
			throw new UserException("Invalid credentials");
		return user;
	}

	public User findById(long userId) throws UserException {
		return userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
	}

	private void authenticate(String username, String password) throws UserException {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new UserException("USER_DISABLED");
        } catch (BadCredentialsException e) {
            throw new UserException("Invalid credentials");
        }

    }
	
	private User mapDtoToUser(UserDTO dtoUser) {
		User user = new User();
		
		user.setBirthday(dtoUser.birthday);
		user.setEmail(dtoUser.email);
		user.setGenre(dtoUser.genre);
		user.setLastname(dtoUser.lastname);
		user.setName(dtoUser.name);
		user.setPassword(pwdEncoder.encode(dtoUser.password));
		
		return user;
	}
	
}
