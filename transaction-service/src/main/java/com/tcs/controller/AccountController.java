package com.tcs.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.service.AccountService;
@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<Map<String, String>> createAccount(
            @PathVariable Long userId) {

        String pin = accountService.createAccount(userId);

        return ResponseEntity.ok(
            Map.of(
                "message", "Account created successfully",
                "pin", pin   // shown ONCE
            )
        );
    }
}
