package com.rodmontiel.ec.service;

import java.util.Date;

import com.rodmontiel.ec.contracts.v1.request.ResetPasswordRq;
import com.rodmontiel.ec.contracts.v1.response.BaseResponse;
import com.rodmontiel.ec.contracts.v1.response.user.ConfirmAccountRs;
import com.rodmontiel.ec.ex.GenericExceptionHandler;
import com.rodmontiel.ec.model.User;
import com.rodmontiel.ec.model.VerificationToken;
import com.rodmontiel.ec.repository.UserRepository;
import com.rodmontiel.ec.repository.VerificationTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenService {
  
	@Autowired
	private BCryptPasswordEncoder pwdEncoder;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private VerificationTokenRepository verificationTokenRepository;
  
  public ConfirmAccountRs confirmAccount(String confirmationToken) throws GenericExceptionHandler, Exception {

    ConfirmAccountRs rs = new ConfirmAccountRs();

    VerificationToken verificationToken = verificationTokenRepository.findByConfirmationToken(confirmationToken)
      .orElseThrow(() -> new GenericExceptionHandler(251));

    var today = new Date();

    if(verificationToken.getExpirationDate().compareTo(today) < 0) {
      rs.code = 252;
      rs.error = "Confirmation token expired";
      rs.success = false;
    } else {
      User user = verificationToken.getUser();
      user.setEnabled(true);
      userRepository.save(user);
      rs.code = 200;
      rs.error = null;
      rs.success = true;
    }

    return rs;
  }

  public BaseResponse resetPassword(ResetPasswordRq request) throws GenericExceptionHandler, Exception {
    
    BaseResponse rs = new BaseResponse();

    VerificationToken verificationToken = verificationTokenRepository
      .findByConfirmationToken(request.token)
      .orElseThrow(() -> new GenericExceptionHandler(251));

    User user = verificationToken.getUser();

    user.setPassword(pwdEncoder.encode(request.password));
    userRepository.save(user);

    rs.code = 200;
    rs.success = true;

    return rs;
  }
  
}
