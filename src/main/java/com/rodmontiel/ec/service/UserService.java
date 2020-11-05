package com.rodmontiel.ec.service;

import com.google.gson.Gson;
import com.rodmontiel.ec.contracts.v1.request.UpdatePasswordRq;
import com.rodmontiel.ec.contracts.v1.response.BaseResponse;
import com.rodmontiel.ec.contracts.v1.response.user.GetProfileRs;
import com.rodmontiel.ec.contracts.v1.response.user.LoginRs;
import com.rodmontiel.ec.contracts.v1.response.user.OAuth2LoginRs;
import com.rodmontiel.ec.contracts.v1.response.user.SignupRs;
import com.rodmontiel.ec.dto.UserDTO;
import com.rodmontiel.ec.ex.GenericExceptionHandler;
import com.rodmontiel.ec.model.User;
import com.rodmontiel.ec.model.VerificationToken;
import com.rodmontiel.ec.repository.UserRepository;
import com.rodmontiel.ec.repository.VerificationTokenRepository;
import com.rodmontiel.ec.security.JwtToken;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private Logger gLogger;
	@Autowired
	private JwtToken jwtToken;
	@Autowired
	private BCryptPasswordEncoder pwdEncoder;
	@Autowired
	private UserDetailsService udService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private VerificationTokenRepository tokenRepository;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private HttpClient httpClient;

	@Value("${spring.mail.username}")
	private String fromEmail;

	public LoginRs login(String authData) throws GenericExceptionHandler, Exception {
		LoginRs rs = new LoginRs();

		String[] arrAuthData = authData.split(" ");

		if (arrAuthData.length <= 0)
			throw new GenericExceptionHandler(110);

		String strLoginData = new String(Base64.getDecoder().decode(arrAuthData[1]));
		String[] arrLoginData = strLoginData.split(":");

		if (arrLoginData.length <= 0)
			throw new GenericExceptionHandler(110);

		String email = arrLoginData[0];
		String pwd = arrLoginData[1];

		authenticate(email, pwd);

		final UserDetails userDetails = udService.loadUserByUsername(email);
		User user = userRepository.getUserByEmail(email)
				.orElseThrow(() -> new GenericExceptionHandler(111));

		/*
		 * if(!user.isEnabled()) { throw new GenericExceptionHandler(119,
		 * "Account not active"); }
		 */

		String token = jwtToken.generateToken(userDetails);

		rs.email = email;
		rs.name = user.getName();
		rs.token = token;
		rs.success = true;

		return rs;
	}

	public SignupRs signup(UserDTO dtoUser) throws GenericExceptionHandler, Exception {
		SignupRs rs = new SignupRs();

		User user = userRepository.getUserByEmail(dtoUser.email).orElse(null);

		if (user != null)
			throw new GenericExceptionHandler(112);

		User userToSave = mapDtoToUser(dtoUser);
		userToSave.setEnabled(false);
		var now = new java.sql.Date(new java.util.Date().getTime());
		userToSave.setCreationDate(now);
		userToSave.setUpdateDate(now);
		// userToSave.setTokenExpired(true);

		user = userRepository.save(userToSave);

		try {

			VerificationToken token = new VerificationToken(user);
			tokenRepository.save(token);

			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(userToSave.getEmail());
			msg.setSubject("Confirm registration!");
			msg.setFrom(fromEmail);
			msg.setText("Hello from Expenses Control WebApp!\n"
					+ "Thank you for your registration, the final step it's to verify your account please click here:\n"
					+ "http://localhost:3000/confirm-account?token=" + token.getToken());

			javaMailSender.send(msg);
			rs.success = true;
			rs.signupMessage = "User registered successfully";

		} catch (Exception ex) {
			gLogger.error("--------------------------------------------------------------------------------");
			gLogger.error("Ha ocurrido un error al enviar el mail de activacion");
			ex.printStackTrace();
			gLogger.error("--------------------------------------------------------------------------------");
			rs.success = false;
			rs.signupMessage = "El usuario se ha registrado correctamente, "
					+ "pero ha ocurrido un error al enviar el email de activacion. "
					+ "Por favor envia un email a support@rodmontiel.com; " + "Lamentamos las inconveniencas";
		}

		return rs;
	}

	public BaseResponse requestResetPassword(String email) throws GenericExceptionHandler, Exception {
		BaseResponse rs = new BaseResponse();

		User user = userRepository.getUserByEmail(email).orElse(null);

		if (user != null) {

			try {

				VerificationToken token = new VerificationToken(user);
				tokenRepository.save(token);

				SimpleMailMessage msg = new SimpleMailMessage();
				msg.setTo(user.getEmail());
				msg.setSubject("Reset password");
				msg.setFrom(fromEmail);
				msg.setText("Hello from Expenses Control WebApp!\n" + "Click the next link to reset your password:\n"
						+ "http://localhost:3000/reset-password-form?token=" + token.getToken());

				javaMailSender.send(msg);

			} catch (Exception ex) {
				gLogger.error("--------------------------------------------------------------------------------");
				gLogger.error("Ha ocurrido un error al enviar el mail para resetear la contraseña");
				ex.printStackTrace();
				gLogger.error("--------------------------------------------------------------------------------");
			}

		}

		rs.success = true;
		rs.code = 200;

		return rs;

	}

	public User searchUserByEmail(String email) throws GenericExceptionHandler {
		User user = userRepository.getUserByEmail(email).orElse(null);
		if (user == null)
			throw new GenericExceptionHandler(110);
		return user;
	}

	public User findById(long userId) throws GenericExceptionHandler {
		return userRepository.findById(userId)
				.orElseThrow(() -> new GenericExceptionHandler(113));
	}

	private void authenticate(String username, String password) throws GenericExceptionHandler {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new GenericExceptionHandler(114);
		} catch (BadCredentialsException e) {
			throw new GenericExceptionHandler(110);
		}
	}

	public OAuth2LoginRs oauth2Login(String code) throws GenericExceptionHandler, Exception {
		OAuth2LoginRs rs = new OAuth2LoginRs();

		Map<Object, Object> data = new HashMap<>();
		data.put("client_id", "63b665c5a28378d7c372");
		data.put("client_secret", "5b26326beb07666983c1e5f8d14ed5801900b05d");
		data.put("code", code);

		HttpRequest request = HttpRequest.newBuilder().POST(buildFormDataFromMap(data))
				.uri(URI.create("https://github.com/login/oauth/access_token"))
				.header("Content-Type", "application/x-www-form-urlencoded").build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		if (response.statusCode() == 200 && !response.body().isBlank() && !response.body().contains("error=")
				&& response.body().contains("access_token=")) {
			var res = response.body().split("&");
			Map<String, String> vars = new HashMap<>();
			for (String v : res) {
				var splittedVar = v.split("=");
				if (splittedVar.length > 0) {
					vars.put(splittedVar[0], splittedVar[1]);
				}
			}

			request = HttpRequest.newBuilder().GET().uri(URI.create("https://api.github.com/user"))
					.setHeader("Authorization", "bearer " + vars.get("access_token")).build();

			HttpResponse<String> response2 = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if (response2.statusCode() == 200 && !response2.body().isBlank() && !response2.body().contains("error=")) {

				UserDTO userDTO = new Gson().fromJson(response2.body(), UserDTO.class);

				if (userDTO != null && userDTO.email != null && !userDTO.email.isBlank()) {
					try {

						final UserDetails userDetails = udService.loadUserByUsername(userDTO.email);

						User user = userRepository.getUserByEmail(userDTO.email).orElse(null);

						if (!user.isEnabled()) {
							throw new GenericExceptionHandler(119);
						}

						String token = jwtToken.generateToken(userDetails);
						rs.token = token;
						rs.code = 200;
						rs.name = user.getName();
						rs.email = userDTO.email;
						rs.success = true;

					} catch (GenericExceptionHandler gex) {
						if (gex.code == 113) {
							rs.code = 211;
							rs.name = userDTO.name;
							rs.birthday = userDTO.birthday;
							rs.email = userDTO.email;
							rs.success = true;
						}
					}
				} else {
					throw new GenericExceptionHandler(116);
				}
			} else {
				gLogger.error("--------------------------------------------------------------------------------");
				gLogger.error("An error has ocurring trying to get Gitgub User Information");
				gLogger.error("---> statusCode: " + response2.statusCode());
				gLogger.error("---------> body: " + response2.body());
				gLogger.error("--------------------------------------------------------------------------------");

				throw new Exception("An error has ocurred");
			}
		} else {
			gLogger.error("--------------------------------------------------------------------------------");
			gLogger.error("An error has ocurring trying to get Gitgub Access token");
			gLogger.error("---> statusCode: " + response.statusCode());
			gLogger.error("---------> body: " + response.body());
			gLogger.error("--------------------------------------------------------------------------------");
			throw new Exception("An error has ocurred");
		}

		return rs;
	}

	public GetProfileRs getProfile(String authData) throws GenericExceptionHandler, Exception {
		GetProfileRs rs = new GetProfileRs();

		String email = jwtToken.getUsernameFromAuthorization(authData);

		User user = userRepository.getUserByEmail(email)
				.orElseThrow(() -> new GenericExceptionHandler(113));

		rs.user = mapUserToDto(user);
		rs.success = true;

		return rs;
	}

	public BaseResponse updatePassword(String authData, UpdatePasswordRq request)
			throws GenericExceptionHandler, Exception {
		BaseResponse rs = new BaseResponse();

		Boolean errorFlag = false;

		String email = jwtToken.getUsernameFromAuthorization(authData);

		User user = userRepository.getUserByEmail(email)
				.orElseThrow(() -> new GenericExceptionHandler(113));

		try {
			authenticate(email, request.oldPassword);
		} catch (GenericExceptionHandler gex) {
			errorFlag = true;
			if (gex.code == 110) {
				rs.code = 120;
				rs.error = "Incorrect password";
				rs.success = false;
			} else {
				throw gex;
			}
		}

		if (!errorFlag) {
			user.setPassword(pwdEncoder.encode(request.newPassword));
			user.setUpdateDate(new java.sql.Date(new java.util.Date().getTime()));
			userRepository.save(user);
			rs.code = 200;
			rs.success = true;
		}
		return rs;
	}

	public BaseResponse updateProfile(String authData, UserDTO userDto) throws GenericExceptionHandler, Exception {
		BaseResponse rs = new BaseResponse();

		String email = jwtToken.getUsernameFromAuthorization(authData);

		User user = userRepository.getUserByEmail(email)
				.orElseThrow(() -> new GenericExceptionHandler(113));

		user.setBirthday(userDto.birthday);
		user.setGenre(userDto.genre);
		user.setLastname(userDto.lastname);
		user.setName(userDto.name);
		user.setUpdateDate(new java.sql.Date(new java.util.Date().getTime()));

		userRepository.save(user);

		rs.code = 200;
		rs.success = true;

		return rs;
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

	private UserDTO mapUserToDto(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.birthday = user.getBirthday();
		userDTO.email = user.getEmail();
		userDTO.genre = user.getGenre();
		userDTO.lastname = user.getLastname();
		userDTO.name = user.getName();
		return userDTO;
	}

	private HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
		var builder = new StringBuilder();
		for (Map.Entry<Object, Object> entry : data.entrySet()) {
			if (builder.length() > 0) {
				builder.append("&");
			}
			builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
			builder.append("=");
			builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
		}
		return HttpRequest.BodyPublishers.ofString(builder.toString());
	}

}
