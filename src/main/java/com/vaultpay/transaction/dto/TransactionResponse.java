package com.vaultpay.transaction.dto;

import com.vaultpay.transaction.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {
    private TransactionType type;
    private BigDecimal amount;
    private String fromAccount;
    private String toAccount;
    private String description;
    private LocalDateTime timestamp;
}
