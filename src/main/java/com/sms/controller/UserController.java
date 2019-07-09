package com.sms.controller;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;



import  com.sms.payload.JwtAuthenticationResponse;
import  com.sms.payload.LoginRequest;
import com.sms.exception.ResourceNotFoundException;
import com.sms.model.UserProfile;
import com.sms.security.CurrentUser;
import com.sms.security.UserPrincipal;
import com.sms.payload.UserIdentityAvailability;
import com.sms.exception.AppException;
import com.sms.model.Role;
import com.sms.model.RoleName;
import com.sms.model.User;
import com.sms.payload.ApiResponse;
import com.sms.payload.SignUpRequest;
import com.sms.payload.TokenRequest;
import com.sms.payload.UserProfileRequest;
import com.sms.repository.RoleRepository;
import com.sms.repository.UserProfileRepository;
import com.sms.repository.UserRepository;
import com.sms.security.JwtTokenProvider;
import com.sms.service.UploadService;
//import com.sms.security.JwtTokenProvider;
import com.sms.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1/user")
@Api(value="user", description = "Data service operations on Users", tags=("users"))
public class UserController {
	
	    protected Logger logger = LoggerFactory.getLogger(UserController.class);
	    
	    @Autowired
	    AuthenticationManager authenticationManager;
	    
	    @Autowired
	    private UploadService uploadService;
	    
	    @Autowired
	    private UserService userService;
	    
	    @Autowired
	    UserRepository userRepository;

	    @Autowired
	    RoleRepository roleRepository;
	    
	    @Autowired
	    UserProfileRepository userProfileRepository;

	    @Autowired
	    PasswordEncoder passwordEncoder;

	    @Autowired
	    JwtTokenProvider tokenProvider;
	    
	    
	    @PostMapping("/getToken")
	    @ApiOperation(value="Get the JWT Token", notes="Generates a random JWT token based on Credentials Provided",produces = "application/json", nickname="getToken")
	    public ResponseEntity<?> getToken(@Valid @RequestBody TokenRequest tokenRequest) {

	        Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                		tokenRequest.getUsernameOrEmail(),
	                		tokenRequest.getPassword()
	                )
	        );

	        String jwt = tokenProvider.generateToken(authentication);
	        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	    }


		@PostMapping("/signup")
		@ApiOperation(value="SignUp", notes="Registers the user", nickname="registerUser")
	    public  ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) throws Exception 
	    {
	    	//logger.info(String.format("user-service registerUser() invoked: %s for %s", signUpRequest.getName())); 
			 User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(), passwordEncoder.encode(signUpRequest.getPassword()), UUID.randomUUID().toString());
		        
			 if(userRepository.existsByUsername(user.getUsername())) {
		            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
		                    HttpStatus.BAD_REQUEST);
		        }

		        if(userRepository.existsByEmail(user.getEmail())) {
		            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
		                    HttpStatus.BAD_REQUEST);
		        }

		        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
		                .orElseThrow(() -> new AppException("User Role not set."));

		        user.setRoles(Collections.singleton(userRole));

		        try {
		        	User result = userRepository.save(user);
					URI location = ServletUriComponentsBuilder
			                .fromCurrentContextPath().path("/v1/user/{username}")
			                .buildAndExpand(result.getUsername()).toUri();
					
					 return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
				} catch (Exception e) 
		        {
					logger.error("Exception raised registerUser REST Call {0}", e);
					e.printStackTrace();
					return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
				}      
	    }
	    
	    
	    @GetMapping("/checkUsernameAvailability")
	    @ApiOperation(value="checkUsernameAvailability", notes="Checks the Username Availability in the System", nickname="checkUsernameAvailability")
	    public ResponseEntity<?> checkUsernameAvailability(@RequestParam(value = "username") String username) throws Exception
	    {
	    	logger.info("user-service checkUsernameAvailability() invoked"); 	 
	         try {
				 return new ResponseEntity<>(userService.checkUsernameAvailability(username),HttpStatus.OK);
				 
			} catch (Exception e) {
				
				e.printStackTrace();
				logger.error(e.getMessage());
				return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
			}
	    }
	    
	    @GetMapping("/checkEmailAvailability")
	    @ApiOperation(value="checkEmailAvailability", notes="Checks the Email Availability in the System", nickname="checkEmailAvailability")
	    public ResponseEntity<?> checkEmailAvailability(@RequestParam(value = "email") String email) {
	    	
	    	logger.info("user-service checkEmailAvailability() invoked"); 
	    	
	    	 try {
				return new ResponseEntity<>(userService.checkEmailAvailability(email),HttpStatus.OK);
				
			} catch (Exception e)
	    	{
				e.printStackTrace();
				logger.error(e.getMessage());
				return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
			}
	    	
	    }
	    
	    @GetMapping("/UserProfile/{username}")
	    @PreAuthorize("hasRole('USER')")
	    @ApiOperation(value="getUserProfile", notes="Gets the User Profile Details from the System based on the Username provided", nickname="getUserProfile")
	    public Optional<UserProfile> getUserProfile(@CurrentUser UserPrincipal currentUser,@PathVariable(value = "username") String username) {
	        
	    	logger.info("user-service getUserProfile() invoked");
	    	
	    	User user = userRepository.findByUsername(username)
	                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

	        Optional<UserProfile> userProfile;
			try {
				userProfile = userProfileRepository.findByUserId(user.getId());
			       return userProfile;
			} catch (Exception e) {

				logger.error(e.getMessage());
				return null;
			}

	 
	    }
	    
	    @DeleteMapping("/UserProfile/{username}")
	    @PreAuthorize("hasRole('USER')")
	    @ApiOperation(value="deleteUserProfile", notes="Deletes the User Profile Details from the System based on the Username provided", nickname="deleteUserProfile")
	    public ResponseEntity<?> deleteUserProfile(@CurrentUser UserPrincipal currentUser,@PathVariable(value = "username") String username) {
	    	User user = userRepository.findByUsername(username)
	                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
	    	 
	    	 userProfileRepository.deleteByUserId(user.getId());
	    	 return ResponseEntity.ok().build();
	    }
	    
	    @PostMapping("/UserProfile/{username}")
	    @PreAuthorize("hasRole('USER')")
	    @ApiOperation(value="createUserProfile", notes="Creates a User Profile Details in the System for the Username provided", nickname="createUserProfile")
	    public ResponseEntity<?> createUserProfile(@CurrentUser UserPrincipal currentUser,@PathVariable(value = "username") String username,@Valid @RequestBody UserProfileRequest userProfileRequest) {
	        User user = userRepository.findByUsername(username)
	                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

	        UserProfile userProfile = new UserProfile(user,userProfileRequest.getDisplayPic(),userProfileRequest.getAddress(),userProfileRequest.getCity(),userProfileRequest.getPhoneNo());
	        
	        UserProfile result = userProfileRepository.save(userProfile);
			URI location = ServletUriComponentsBuilder
	                .fromCurrentContextPath().path("/v1/user/UserProfile/{username}")
	                .buildAndExpand(username).toUri();
	        
			 return ResponseEntity.created(location).body(new ApiResponse(true, "User Profile Created Successfully"));
	    }
	    
	    @PostMapping("/UserProfile/upload/{username}")
	    @ApiOperation(value="upload", notes="Upload the Image file", nickname="upload")
	    public ResponseEntity<?> upload(@PathVariable(value = "username") String username,@RequestParam("file") MultipartFile File)throws Exception {
	    	  return uploadService.saveImage(File);
								  
	    }
	    


}
