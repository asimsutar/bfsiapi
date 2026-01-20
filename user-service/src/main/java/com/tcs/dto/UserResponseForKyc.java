package com.tcs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseForKyc {
	private Long id;
    private String username;
    private String password;
    private String role;
    private String kycStatus;

    // 🔥 THIS WAS MISSING
    private String pan;

}
