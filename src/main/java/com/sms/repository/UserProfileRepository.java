package com.sms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sms.model.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
	

    List<UserProfile> findByIdIn(List<Long> userIds);
   Optional<UserProfile> findByUserId(Long userId);
   UserProfile findUserProfileByUserId(Long userId);
   //Optional<UserProfile> findByUuid(String uuid);
   UserProfile save(Optional<UserProfile> userProfile);
   
   @Modifying
   @Transactional
   @Query("delete from UserProfile where user_id = :userId")
   void deleteByUserId(@Param("userId") Long userId);
   
   //@Query("SELECT up FROM user_profile up where up.uuid = :userUuid")
  // Optional<UserProfile> findByUserUuid(@Param("Uuid") String userUuid);
 
}
