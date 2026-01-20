package com.tcs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.dto.InternalUserResponse;
import com.tcs.dto.UserRegisterRequest;
import com.tcs.dto.UserResponse;
import com.tcs.dto.UserResponseForKyc;
import com.tcs.entity.User;
import com.tcs.repository.UserRepository;
import com.tcs.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository repository;
	
	@PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody UserRegisterRequest request) {

        return ResponseEntity.ok(userService.register(request));
    }
	@GetMapping("/username/{username}")
	public User getByUsername(@PathVariable String username) {
	    return repository.findByUsername(username);
	}
	
	@GetMapping("/usernamekyc/{username}")
	public UserResponseForKyc getByUsernameforKyc(@PathVariable String username) {

	    User user = repository.findByUsername(username);

	    UserResponseForKyc response = new UserResponseForKyc();
	    response.setId(user.getId());
	    response.setUsername(user.getUsername());
	    response.setPassword(user.getPassword());
	    response.setRole(user.getRole());
	    response.setKycStatus(user.getKycStatus());

	    //  THIS WAS MISSING
	    response.setPan(user.getPanNumber());

	    return response;
	}
	
	@GetMapping("/internal/username/{username}")
	public InternalUserResponse getInternalUser(@PathVariable String username) {

	    User user = repository.findByUsername(username);

	    return new InternalUserResponse(
	        user.getId(),
	        user.getUsername(),
	        user.getKycStatus()
	    );
	}

	
}
