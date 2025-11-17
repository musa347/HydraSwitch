package com.hydraswitch.core.dto;

import lombok.Data;

import java.time.Instant;
import java.util.Map;
@Data
public class SwitchResponse {
    private String switchRef;
    private String clientReference;
    private String status; // SUCCESS, FAILED, PENDING, REVERSED
    private String responseCode;
    private String responseMessage;
    private String bankResponseCode;
    private String bankResponseMessage;
    private Instant timestamp;
    private Integer latencyMs;
    private Map<String, Object> metadata;

    public SwitchResponse() {}

    // convenience factory helpers
    public static SwitchResponse success(String switchRef, String clientRef, String bankCode, String bankMsg, int latency) {
        SwitchResponse r = new SwitchResponse();
        r.switchRef = switchRef;
        r.clientReference = clientRef;
        r.status = "SUCCESS";
        r.responseCode = "00";
        r.responseMessage = "Approved";
        r.bankResponseCode = bankCode;
        r.bankResponseMessage = bankMsg;
        r.timestamp = Instant.now();
        r.latencyMs = latency;
        return r;
    }

    public static SwitchResponse failed(String switchRef, String code, String message) {
        SwitchResponse r = new SwitchResponse();
        r.switchRef = switchRef;
        r.status = "FAILED";
        r.responseCode = code;
        r.responseMessage = message;
        r.timestamp = Instant.now();
        return r;
    }
}
