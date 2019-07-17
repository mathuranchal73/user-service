package com.sms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sms.model.User;
import com.sms.model.VerificationToken;
import com.sms.payload.SignUpRequest;
import com.sms.payload.UserIdentityAvailability;


public interface UserService {
	

	public UserIdentityAvailability checkUsernameAvailability(String username);

	public UserIdentityAvailability checkEmailAvailability(String email);
	
	public static void createVerificationToken(User user, String token) {
		// TODO Auto-generated method stub
		
	}

	public VerificationToken getVerificationToken(String verificationToken);

	public void enableRegisteredUser(User user);

}
