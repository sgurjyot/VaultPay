package com.vaultpay.transaction.dto;

import lombok.Data;

@Data
public class WithdrawRequest {
    private String accountNumber;
    private String amount;
    private String description;
}
