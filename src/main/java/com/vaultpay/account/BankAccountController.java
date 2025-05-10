package com.vaultpay.account;

import com.vaultpay.account.dto.BankAccountResponse;
import com.vaultpay.account.dto.CreateAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService accountService;

    @PostMapping
    public ResponseEntity<BankAccountResponse> createAccount(
            @RequestBody CreateAccountRequest request,
            @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseEntity.ok(accountService.createAccount(request, user.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<BankAccountResponse>> listUserAccounts(
            @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseEntity.ok(accountService.getUserAccounts(user.getUsername()));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<BankAccountResponse> getAccountDetails(
            @PathVariable String accountNumber,
            @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseEntity.ok(accountService.getAccountDetails(accountNumber, user.getUsername()));
    }
}
