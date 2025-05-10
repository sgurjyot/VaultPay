package com.vaultpay.account;

import com.vaultpay.account.dto.BankAccountResponse;
import com.vaultpay.account.dto.CreateAccountRequest;
import com.vaultpay.security.user.User;
import com.vaultpay.security.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public BankAccountResponse createAccount(CreateAccountRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BankAccount account = BankAccount.builder()
                .accountNumber(UUID.randomUUID().toString())
                .balance(new BigDecimal(request.getInitialDeposit()))
                .createdAt(LocalDateTime.now())
                .owner(user)
                .build();

        BankAccount saved = accountRepository.save(account);

        return mapToResponse(saved);
    }

    public List<BankAccountResponse> getUserAccounts(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return accountRepository.findAllByOwner(user).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public BankAccountResponse getAccountDetails(String accountNumber, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BankAccount account = accountRepository
                .findByAccountNumberAndOwner(accountNumber, user)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return mapToResponse(account);
    }

    private BankAccountResponse mapToResponse(BankAccount account) {
        return BankAccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .createdAt(account.getCreatedAt())
                .build();
    }
}
