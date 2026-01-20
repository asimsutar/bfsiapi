package com.tcs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
	private String accountNumber;
	private Long id;
    private String username;
    private String kycStatus;
    private String message;
    
 // THIS WAS MISSING
    private String pin; 
}
