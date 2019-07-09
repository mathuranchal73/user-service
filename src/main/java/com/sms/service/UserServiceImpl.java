package com.sms.service;

import java.net.URI;
import java.util.Collections;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

//import com.sms.security.JwtTokenProvider;
import com.sms.exception.AppException;
import com.sms.model.Role;
import com.sms.model.RoleName;
import com.sms.model.User;
import com.sms.payload.ApiResponse;
import com.sms.payload.SignUpRequest;
import com.sms.payload.UserIdentityAvailability;
import com.sms.repository.RoleRepository;
import com.sms.repository.UserRepository;
import com.sms.security.JwtTokenProvider;

@Service
public class UserServiceImpl implements UserService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
    AuthenticationManager authenticationManager;

 	@Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

	

	@Override
	public UserIdentityAvailability checkUsernameAvailability(String username) {
		 Boolean isAvailable;
		 
		try {
			isAvailable = !userRepository.existsByUsername(username);
			
			return new UserIdentityAvailability(isAvailable);
			
		} catch (Exception e) 
		{
			logger.error("Exception raised checkUsernameAvailability REST Call {0}", e);
			e.printStackTrace();
			return null;
		}
		
		 
	}

	@Override
	public UserIdentityAvailability checkEmailAvailability(String email) {
		Boolean isAvailable;
		try {
			isAvailable = !userRepository.existsByEmail(email);
			return new UserIdentityAvailability(isAvailable);
			
		} catch (Exception e) 
		{
			logger.error("Exception raised checkEmailAvailability REST Call {0}", e);
			e.printStackTrace();
			return null;
		}
	}
    
    
    
    

}
