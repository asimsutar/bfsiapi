package com.tcs.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tcs.entity.Account;
import com.tcs.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String createAccount(Long userId) {

        if (accountRepository.existsByUserId(userId)) {
            throw new RuntimeException("Account already exists");
        }

        // ✅ Generate 6-digit PIN
        String rawPin = String.valueOf(
                100000 + new SecureRandom().nextInt(900000)
        );

        Account account = new Account();
        account.setUserId(userId);
        account.setBalance(BigDecimal.ZERO);
        account.setPin(passwordEncoder.encode(rawPin));
        account.setStatus("ACTIVE");

        accountRepository.save(account);

        return rawPin; // return only once
    }
}
