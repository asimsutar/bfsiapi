package com.tcs.dto;

import lombok.Data;

@Data
public class KycSubmitRequest {
	private String panNumber;
    private String documentType;
    private String documentUrl;
}
