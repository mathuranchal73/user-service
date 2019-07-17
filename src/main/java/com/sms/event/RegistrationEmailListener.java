package com.sms.event;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sms.kafka.Producer;
import com.sms.model.User;
import com.sms.service.UserServiceImpl;

@Component
public class RegistrationEmailListener implements ApplicationListener<OnRegistrationSuccessEvent> {
	
	@Autowired
	private UserServiceImpl userService;
	
	 @Autowired
	 Producer producer;

	@Override
	public void onApplicationEvent(OnRegistrationSuccessEvent event) {
		this.confirmRegistration(event);	
	}

	private void confirmRegistration(OnRegistrationSuccessEvent event) {
		
		User user=event.getUser();
		String token= UUID.randomUUID().toString();
		userService.createVerificationToken(user, token);
		String recipient = user.getEmail();
		String subject = "Registration Confirmation";
		
		URI url = ServletUriComponentsBuilder
                .fromCurrentContextPath().path(event.getAppUrl()+"/v1/user/confirmRegistration?token={token}")
                .buildAndExpand(token).toUri();
		producer.sendMessage(url.toString());
		
	}
	

}
