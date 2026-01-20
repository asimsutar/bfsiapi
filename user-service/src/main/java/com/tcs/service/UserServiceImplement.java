package com.tcs.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tcs.dto.UserRegisterRequest;
import com.tcs.dto.UserResponse;
import com.tcs.entity.User;
import com.tcs.exception.UserAlreadyExistsException;
import com.tcs.repository.UserRepository;

@Service
public class UserServiceImplement implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${transaction.service.url}")
    private String transactionServiceUrl;

    @Override
    public UserResponse register(UserRegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername()))
            throw new UserAlreadyExistsException("Username already exists");

        if (userRepository.existsByPanNumber(request.getPanNumber()))
            throw new UserAlreadyExistsException("PAN already exists");

        if (userRepository.existsByPhone(request.getPhone()))
            throw new UserAlreadyExistsException("Phone already exists");

        User user = User.builder()
                .accountNumber(generateAccountNumber())
                .fullName(request.getFullName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .panNumber(request.getPanNumber())
                .dob(request.getDob())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .occupation(request.getOccupation())
                .gender(request.getGender())
                .role("ROLE_USER")
                .kycStatus("PENDING")
                .isActive(false)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        // CREATE ACCOUNT + RECEIVE PIN
        Map<?, ?> response = restTemplate.postForObject(
                transactionServiceUrl + "/accounts/create/" + user.getId(),
                null,
                Map.class
        );

        String generatedPin = response.get("pin").toString();

        return UserResponse.builder()
                .accountNumber(user.getAccountNumber())
                .username(user.getUsername())
                .kycStatus(user.getKycStatus())
                .message("Registration successful. KYC pending.")
                .pin(generatedPin)   // NOW INCLUDED
                .build();
    }

    private String generateAccountNumber() {
        return "AC" + System.currentTimeMillis();
    }
}
