package com.tcs.dto;

import lombok.Data;

@Data
public class UserResponse {
	private Long id;
    private String username;
    private String pan;
    private String kycStatus;
}
