package com.vaultpay.account.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BankAccountResponse {
    private String accountNumber;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}
