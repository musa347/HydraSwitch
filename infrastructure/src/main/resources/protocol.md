# HydraSwitch — Protocol Specification

This document is the canonical protocol specification for HydraSwitch:
- TransactionRequest: client → API Gateway → Switch Core
- SwitchRequest: Switch Core → Adapters
- SwitchResponse: Adapters → Switch Core → Client

All timestamps use ISO-8601 (UTC). JSON keys use snake_case.

---

## Table of contents
1. Schemas
    - TransactionRequest
    - SwitchRequest
    - SwitchResponse
2. Examples
3. Error codes
4. Headers / Signature rules
5. Idempotency rules
6. Where to place this file & how to validate from Java

---

## 1. Schemas

### 1.1 Shared JSON Schema container (definitions)
Use these definitions to validate messages. This document uses JSON Schema Draft-07.

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "$id": "https://hydraswitch.example.com/schemas/protocol.json",
  "title": "HydraSwitch Protocol Schemas",
  "type": "object",
  "definitions": {

    "TransactionRequest": {
      "title": "TransactionRequest",
      "type": "object",
      "required": [
        "client_reference",
        "operation",
        "amount",
        "currency",
        "destination_account",
        "destination_bank_code"
      ],
      "properties": {
        "client_reference": { "type": "string", "maxLength": 128 },
        "idempotency_key": { "type": "string", "maxLength": 128 },
        "operation": {
          "type": "string",
          "enum": [
            "NAME_ENQUIRY",
            "DEBIT",
            "CREDIT",
            "BALANCE_ENQUIRY",
            "FUND_TRANSFER",
            "BLOCK_AMOUNT",
            "UNBLOCK_AMOUNT"
          ]
        },
        "amount": { "type": "number", "minimum": 0.01 },
        "currency": { "type": "string", "minLength": 3, "maxLength": 3 },
        "source_account": { "type": ["string", "null"] },
        "destination_account": { "type": "string" },
        "destination_bank_code": { "type": "string", "maxLength": 8 },
        "metadata": { "type": ["object", "null"], "additionalProperties": true },
        "requested_at": { "type": "string", "format": "date-time" }
      },
      "additionalProperties": false
    },

    "SwitchRequest": {
      "title": "SwitchRequest",
      "type": "object",
      "required": [
        "switch_ref",
        "operation",
        "amount",
        "currency",
        "destination_account",
        "destination_bank_code",
        "created_at"
      ],
      "properties": {
        "switch_ref": { "type": "string" },
        "client_reference": { "type": ["string", "null"] },
        "operation": { "type": "string" },
        "amount": { "type": "number" },
        "currency": { "type": "string" },
        "source_account": { "type": ["string", "null"] },
        "destination_account": { "type": "string" },
        "destination_bank_code": { "type": "string" },
        "idempotency_key": { "type": ["string", "null"] },
        "metadata": { "type": ["object", "null"] },
        "created_at": { "type": "string", "format": "date-time" }
      },
      "additionalProperties": false
    },

    "SwitchResponse": {
      "title": "SwitchResponse",
      "type": "object",
      "required": [
        "switch_ref",
        "status",
        "response_code",
        "response_message",
        "timestamp"
      ],
      "properties": {
        "switch_ref": { "type": "string" },
        "client_reference": { "type": ["string", "null"] },
        "status": {
          "type": "string",
          "enum": ["SUCCESS", "FAILED", "PENDING", "REVERSED"]
        },
        "response_code": { "type": "string" },
        "response_message": { "type": "string" },
        "bank_response_code": { "type": ["string", "null"] },
        "bank_response_message": { "type": ["string", "null"] },
        "timestamp": { "type": "string", "format": "date-time" },
        "latency_ms": { "type": "integer" },
        "metadata": { "type": ["object", "null"], "additionalProperties": true }
      },
      "additionalProperties": false
    }

  }
}
