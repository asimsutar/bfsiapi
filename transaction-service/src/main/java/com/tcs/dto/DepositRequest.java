package com.tcs.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DepositRequest {
	private BigDecimal amount;
}
