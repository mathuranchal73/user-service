package com.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sms.model.Role;
import com.sms.model.User;
import com.sms.model.VerificationToken;

@Repository
public interface TokenRepository extends JpaRepository<VerificationToken, Long> {
	
	public VerificationToken findByToken(String token);
	
	public VerificationToken findByUser(User user);
	
	

}
