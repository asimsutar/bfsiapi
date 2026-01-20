package com.tcs.service;

import com.tcs.dto.UserRegisterRequest;
import com.tcs.dto.UserResponse;

public interface UserService {
	public UserResponse register(UserRegisterRequest request);
	
}
