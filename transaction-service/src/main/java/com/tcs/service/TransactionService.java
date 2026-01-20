package com.tcs.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tcs.dto.BalanceResponse;
import com.tcs.dto.InternalUserResponse;
import com.tcs.dto.TransferRequest;
import com.tcs.dto.UserResponse;
import com.tcs.dto.WithdrawRequest;
import com.tcs.entity.Account;
import com.tcs.entity.Transaction;
import com.tcs.exception.TransactionException;
import com.tcs.repository.AccountRepository;
import com.tcs.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    // 🔁 Resolve username → userId
    private UserResponse getUser(String username) {
        return restTemplate.getForObject(
            userServiceUrl + "/users/internal/username/" + username,
            UserResponse.class
        );
    }

    private InternalUserResponse getUserInternal(String username) {
        return restTemplate.getForObject(
            userServiceUrl + "/users/internal/username/" + username,
            InternalUserResponse.class
        );
    }

    // BALANCE
    public BalanceResponse getBalance(String username) {
        UserResponse user = getUser(username);
        Account account = accountRepository.findByUserId(user.getId());
        return new BalanceResponse(account.getBalance());
    }

    // DEPOSIT (no PIN)
    public void deposit(String username, BigDecimal amount) {
        UserResponse user = getUser(username);
        Account account = accountRepository.findByUserId(user.getId());

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        transactionRepository.save(
                new Transaction(
                        null,
                        account.getId(),
                        "DEPOSIT",
                        amount,
                        "Cash deposit",
                        LocalDateTime.now()
                )
        );
    }

    // WITHDRAW (PIN required)
    public void withdraw(String username, WithdrawRequest request) {
        UserResponse user = getUser(username);
        Account account = accountRepository.findByUserId(user.getId());

        if (!passwordEncoder.matches(request.getPin(), account.getPin())) {
            throw new TransactionException("Invalid PIN");
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new TransactionException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        transactionRepository.save(
                new Transaction(
                        null,
                        account.getId(),
                        "WITHDRAW",
                        request.getAmount(),
                        "Cash withdrawal",
                        LocalDateTime.now()
                )
        );
    }

    // TRANSFER (PIN required)
    public void transfer(String username, TransferRequest request) {
        UserResponse fromUser = getUser(username);

        Account from = accountRepository.findByUserId(fromUser.getId());
        Account to = accountRepository.findByUserId(request.getToUserId());

        if (!passwordEncoder.matches(request.getPin(), from.getPin())) {
            throw new TransactionException("Invalid PIN");
        }

        if (from.getBalance().compareTo(request.getAmount()) < 0) {
            throw new TransactionException("Insufficient balance");
        }

        from.setBalance(from.getBalance().subtract(request.getAmount()));
        to.setBalance(to.getBalance().add(request.getAmount()));

        accountRepository.save(from);
        accountRepository.save(to);

        transactionRepository.save(
                new Transaction(
                        null,
                        from.getId(),
                        "TRANSFER",
                        request.getAmount(),
                        "Transfer to user " + request.getToUserId(),
                        LocalDateTime.now()
                )
        );
    }

    // TRANSACTION HISTORY
    public List<Transaction> history(String username) {
        InternalUserResponse user = getUserInternal(username);
        Account account = accountRepository.findByUserId(user.getId());
        return transactionRepository.findByAccountId(account.getId());
    }
}
