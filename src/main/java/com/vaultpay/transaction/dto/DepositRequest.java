package com.vaultpay.transaction.dto;

import lombok.Data;

@Data
public class DepositRequest {
    private String accountNumber;
    private String amount;
    private String description;
}
