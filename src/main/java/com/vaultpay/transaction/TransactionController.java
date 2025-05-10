package com.vaultpay.transaction;

import com.vaultpay.transaction.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @RequestBody DepositRequest request,
            @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseEntity.ok(transactionService.deposit(request, user.getUsername()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(
            @RequestBody WithdrawRequest request,
            @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseEntity.ok(transactionService.withdraw(request, user.getUsername()));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @RequestBody TransferRequest request,
            @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseEntity.ok(transactionService.transfer(request, user.getUsername()));
    }
}
