package com.vaultpay.transaction;

import com.vaultpay.account.BankAccount;
import com.vaultpay.account.BankAccountRepository;
import com.vaultpay.security.user.User;
import com.vaultpay.security.user.UserRepository;
import com.vaultpay.transaction.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository accountRepository;
    private final UserRepository userRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private BankAccount getUserAccount(String accountNumber, User user) {
        return accountRepository.findByAccountNumberAndOwner(accountNumber, user)
                .orElseThrow(() -> new RuntimeException("Account not found or unauthorized"));
    }

    @Transactional
    public TransactionResponse deposit(DepositRequest request, String email) {
        User user = getUser(email);
        BankAccount account = getUserAccount(request.getAccountNumber(), user);

        BigDecimal amount = new BigDecimal(request.getAmount());
        account.setBalance(account.getBalance().add(amount));

        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .type(TransactionType.DEPOSIT)
                .amount(amount)
                .description(request.getDescription())
                .timestamp(LocalDateTime.now())
                .toAccount(account)
                .build();

        transactionRepository.save(transaction);

        return mapToResponse(transaction);
    }

    @Transactional
    public TransactionResponse withdraw(WithdrawRequest request, String email) {
        User user = getUser(email);
        BankAccount account = getUserAccount(request.getAccountNumber(), user);

        BigDecimal amount = new BigDecimal(request.getAmount());

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .type(TransactionType.WITHDRAW)
                .amount(amount)
                .description(request.getDescription())
                .timestamp(LocalDateTime.now())
                .fromAccount(account)
                .build();

        transactionRepository.save(transaction);

        return mapToResponse(transaction);
    }

    @Transactional
    public TransactionResponse transfer(TransferRequest request, String email) {
        User user = getUser(email);
        BankAccount from = getUserAccount(request.getFromAccount(), user);
        BankAccount to = accountRepository.findByAccountNumberAndOwner(request.getToAccount(), user)
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (from.getBalance().compareTo(new BigDecimal(request.getAmount())) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        BigDecimal amount = new BigDecimal(request.getAmount());

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        accountRepository.save(from);
        accountRepository.save(to);

        Transaction transaction = Transaction.builder()
                .type(TransactionType.TRANSFER)
                .amount(amount)
                .description(request.getDescription())
                .timestamp(LocalDateTime.now())
                .fromAccount(from)
                .toAccount(to)
                .build();

        transactionRepository.save(transaction);

        return mapToResponse(transaction);
    }

    private TransactionResponse mapToResponse(Transaction t) {
        return TransactionResponse.builder()
                .type(t.getType())
                .amount(t.getAmount())
                .fromAccount(t.getFromAccount() != null ? t.getFromAccount().getAccountNumber() : null)
                .toAccount(t.getToAccount() != null ? t.getToAccount().getAccountNumber() : null)
                .description(t.getDescription())
                .timestamp(t.getTimestamp())
                .build();
    }

    public List<TransactionResponse> getTransactionHistory(String accountNumber, String userEmail) {
        User user = getUser(userEmail);
        BankAccount account = getUserAccount(accountNumber, user);

        return transactionRepository
                .findAllByFromAccountOrToAccount(account, account)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

}
