package com.vaultpay.graphql;

import com.vaultpay.account.BankAccountService;
import com.vaultpay.account.dto.BankAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountGraphQLResolver {

    private final BankAccountService bankAccountService;

    @QueryMapping
    public List<BankAccountResponse> myAccounts(@AuthenticationPrincipal UserDetails user) {
        return bankAccountService.getUserAccounts(user.getUsername());
    }
}
