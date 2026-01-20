package com.tcs.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserRegisterRequest {
	private String fullName;
    private String username;
    private String password;
    private String panNumber;
    private LocalDate dob;
    private String email;
    private String phone;
    private String address;
    private String occupation;
    private String gender;
}
