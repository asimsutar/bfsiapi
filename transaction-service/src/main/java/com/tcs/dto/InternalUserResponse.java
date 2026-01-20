package com.tcs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternalUserResponse {
	  private Long id;
	    private String username;
	    private String kycStatus;
}
