package com.vaultpay.graphql;

import com.vaultpay.transaction.TransactionService;
import com.vaultpay.transaction.dto.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransactionGraphQLResolver {

    private final TransactionService transactionService;

    @QueryMapping
    public List<TransactionResponse> accountTransactions(
            @Argument String accountNumber,
            @AuthenticationPrincipal UserDetails user
    ) {
        return transactionService.getTransactionHistory(accountNumber, user.getUsername());
    }
}
