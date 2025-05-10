package com.vaultpay.transaction.dto;

import lombok.Data;

@Data
public class TransferRequest {
    private String fromAccount;
    private String toAccount;
    private String amount;
    private String description;
}
