package com.tcs.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class WithdrawRequest {
	private BigDecimal amount;
    private String pin;
}
