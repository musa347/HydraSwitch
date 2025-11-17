package com.hydraswitch.core.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Data
public class TransactionRequest {
    private String clientReference;
    private String idempotencyKey;
    private String operation;
    private BigDecimal amount;
    private String currency;
    private String sourceAccount;
    private String destinationAccount;
    private String destinationBankCode;
    private Map<String, Object> metadata;
    private Instant requestedAt;
}
