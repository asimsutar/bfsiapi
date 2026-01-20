package com.tcs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tcs.dto.BalanceResponse;
import com.tcs.dto.DepositRequest;
import com.tcs.dto.TransferRequest;
import com.tcs.dto.WithdrawRequest;
import com.tcs.entity.Transaction;
import com.tcs.service.TransactionService;
import com.tcs.util.JwtUtil;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private JwtUtil jwtUtil;

    private String getUsername(String token) {
        return jwtUtil.extractUsername(token);
    }

    @GetMapping("/balance")
    public BalanceResponse balance(
            @RequestHeader("Authorization") String token) {

        return transactionService.getBalance(getUsername(token));
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(
            @RequestHeader("Authorization") String token,
            @RequestBody DepositRequest request) {

        transactionService.deposit(getUsername(token), request.getAmount());
        return ResponseEntity.ok("Deposit successful");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(
            @RequestHeader("Authorization") String token,
            @RequestBody WithdrawRequest request) {

        transactionService.withdraw(getUsername(token), request);
        return ResponseEntity.ok("Withdraw successful");
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(
            @RequestHeader("Authorization") String token,
            @RequestBody TransferRequest request) {

        transactionService.transfer(getUsername(token), request);
        return ResponseEntity.ok("Transfer successful");
    }

    @GetMapping("/history")
    public List<Transaction> history(
            @RequestHeader("Authorization") String token) {

        return transactionService.history(getUsername(token));
    }
}
