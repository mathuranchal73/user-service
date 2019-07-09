package com.sms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sms.model.RoleName;
import com.sms.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	 Optional<Role> findByName(RoleName roleName);

}
