package com.sms.service;

import java.net.URI;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
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
import com.sms.model.VerificationToken;
import com.sms.payload.ApiResponse;
import com.sms.payload.SignUpRequest;
import com.sms.payload.UserIdentityAvailability;
import com.sms.repository.RoleRepository;
import com.sms.repository.TokenRepository;
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
    TokenRepository tokenRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;
    
   /** String booststrapServers="127.0.0.1:9092";
    
    
    //Create Producer Properties
   
   
    
    
    Properties properties= new Properties();
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,booststrapServers);
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
    

    KafkaProducer<String,String> producer= new KafkaProducer<>(properties);
    
    ProducerRecord<String,String> record= new ProducerRecord<>("Welcome_Email","Hello World");
    
    producer.send(record, new Callback() {
    	public void onCompletion
    })
	**/

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
    
	
	public void createVerificationToken(User user, String token) {
		VerificationToken newUserToken = new VerificationToken(token, user);
		//tokenRepository.save(newUserToken);
		tokenRepository.save(newUserToken);
	}
	 
	@Override
	@Transactional
	public VerificationToken getVerificationToken(String verificationToken) {
		return tokenRepository.findByToken(verificationToken);
	}

	@Override
	@Transactional
	public void enableRegisteredUser(User user) {
		if(!user.isEnabled())
		{
		user.setEnabled(true);
		userRepository.save(user);
	}
		else
			return;

}
}
