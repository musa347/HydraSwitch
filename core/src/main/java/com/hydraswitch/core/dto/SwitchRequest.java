package com.hydraswitch.core.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
@Data
public class SwitchRequest {
    private String switchRef;
    private String clientReference;
    private String operation;
    private BigDecimal amount;
    private String currency;
    private String sourceAccount;
    private String destinationAccount;
    private String destinationBankCode;
    private String idempotencyKey;
    private Map<String, Object> metadata;
    private Instant createdAt;
}
