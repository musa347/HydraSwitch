# HydraSwitch

A lightweight, modular, cloud-ready Mini Payment Switch designed to route transactions across multiple bank adapters with high reliability, resilience, and observability.

HydraSwitch simulates real payment switches (similar to Interswitch, NIBSS, Paystack), featuring routing logic, bank connectors, queues, workers, and analytics pipelines.

---

## 1. Project Overview

HydraSwitch enables:

- Receiving payment requests from clients via an API gateway
- Routing requests through a core switching engine
- Invoking different bank adapters
- Ensuring resilience (retries, circuit breakers, timeouts)
- Processing transactions asynchronously
- Delivering analytics and reporting

The system is intentionally designed to reflect modern distributed backend architectures used in production switches.

---

## 2. Functional Requirements

### 2.1 Core Switch Functionality

- Accept incoming API requests (e.g., debit, credit, balance enquiry)
- Validate requests: schema, signature (optional), timestamps
- Route requests to the correct bank adapter
- Aggregate and return the response to the client
- Support synchronous and asynchronous flows

### 2.2 Bank Adapter Layer

Each Bank Adapter must:

- Expose a uniform interface (e.g., `BankAdapter.process(TransactionRequest)`)
- Implement its own HTTP/SOAP call structure
- Handle bank-specific timeouts or failure formats
- Provide mock Bank APIs for simulation

### 2.3 Resilience Engine

- Circuit breaker per bank endpoint
- Retry policies for transient failures
- Request timeouts
- Fallback logic for failed banks (e.g., return “R1: Destination unavailable”)

### 2.4 Queue & Worker Processing

- Switch queues pending transactions (reversals, settlements, retry jobs)
- Worker nodes consume and process queued tasks
- Workers must be horizontally scalable

### 2.5 Reporting & Analytics

- Store all transaction logs in a relational database
- Forward events to analytics database or storage for querying
- Generate dashboards or export CSV reports

### 2.6 Storage & Data

- Store raw requests, responses, status, timing, latency
- Support S3-like storage for archived payloads

### 2.7 Security

- API authentication (API key or JWT)
- Enforce HTTPS
- Audit logs and traceability

---

## 3. Non-Functional Requirements

### 3.1 Performance

- API should handle at least 100–200 requests/sec locally
- Routing must complete within 50–200ms under normal load

### 3.2 Scalability

- Horizontal scaling for:
  - Workers
  - Bank adapters
  - API layer
- Stateless components are encouraged

### 3.3 Reliability

- Circuit breakers prevent cascading failures
- Retriable operations go through the queue system
- Graceful handling of slow or offline banks

### 3.4 Observability

- Centralized logging with correlation IDs
- Metrics:
  - TPS (transactions per second)
  - Success/failure rates
  - Latency
- Distributed tracing support

### 3.5 Security

- No sensitive data stored in logs
- Secure environment variables for bank credentials
- Strict input validation

### 3.6 Extensibility

- Adding a new bank adapter should require minimal changes
- Routing engine must be pluggable

---

## 4. System Design Principles Applied

- **Separation of Concerns:** API, routing engine, bank adapters, workers are isolated  
- **Interface-Driven Development:** All adapters implement the same interface  
- **Hexagonal Architecture:** Domain logic isolated from external services  
- **Event-Driven Processing:** Non-critical tasks flow through queues/workers  
- **Resilience Patterns:** Circuit breaker, retry, timeout, bulkhead  
- **Single Responsibility Principle:** Each module has one job  
- **Fail-Fast & Graceful Degradation:** Quick failure for unreachable banks  
- **Cloud-Native Principles:** Stateless servers, queue-based durability, horizontal scalability

---

## 5. High-Level Architecture Diagram

```text
Client
   ↓
API Gateway
   ↓
Payment Switch API
   ┌─────────────┐
   ↓             ↓
Routing Engine   Resilience
(Core Switch)   (Retries, CB)
   ├─────┐         ├─────┐
   ↓     ↓         ↓     ↓
 GTBank  Zenith    UBA   FirstBank
 Adapter Adapter   Adapter Adapter
