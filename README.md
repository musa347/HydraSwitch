# HydraSwitch
A lightweight, modular, cloud-ready Mini Payment Switch designed to route transactions across multiple bank adapters with high reliability, resilience, and observability.
HydraSwitch simulates how real payment switches operate (similar to Interswitch, NIBSS, Paystack), featuring routing logic, bank connectors, queues, workers, and analytics pipelines.
### 1. Project Overview
HydraSwitch enables:
Receiving payment requests from clients via an API gateway
Routing requests through a core switching engine
Invoking different bank adapters
Ensuring resilience (retries, circuit breakers, timeouts)
Processing transactions asynchronously
Delivering analytics and reporting
The system is intentionally designed to reflect modern distributed backend architectures used in production switches.
### 2. Functional Requirements
#### 2.1 Core Switch Functionality
Accept incoming API requests (e.g., debit, credit, balance enquiry).
Validate requests: schema, signature (optional), timestamps.
Route requests to the correct bank adapter.
Aggregate and return the response to the client.
Support synchronous and asynchronous flows.
#### 2.2 Bank Adapter Layer
Each Bank Adapter must:
Expose a uniform interface (e.g., BankAdapter.process(TransactionRequest)).
Implement its own HTTP/SOAP call structure.
Handle bank-specific timeouts or failure formats.
Mock Bank APIs must be provided for simulation.
#### 2.3 Resilience Engine
Circuit breaker per bank endpoint.
Retry policies for transient failures.
Request timeouts.
Fallback logic for failed banks (e.g., return “R1: Destination unavailable”).
#### 2.4 Queue & Worker Processing
Switch queues pending transactions (e.g., reversals, settlements, retry jobs).
Worker nodes consume and process queued tasks.
Workers must be horizontally scalable.
#### 2.5 Reporting & Analytics
Store all transaction logs in a relational database.
Forward events to analytics database or storage for querying.
Generate simple dashboards or export CSV reports.
#### 2.6 Storage & Data
Store raw requests, responses, status, timing, latency.
Support S3-like storage for archived payloads.
#### 2.7 Security
API authentication (API key or JWT).
Enforce HTTPS.
Audit logs and traceability.

### 3. Non-Functional Requirements
#### 3.1 Performance
API should handle at least 100–200 requests/sec locally.
Routing must complete within 50–200ms under normal load.
#### 3.2 Scalability
Horizontal scaling for:
Workers
Bank adapters
API layer
Stateless components are encouraged.
#### 3.3 Reliability
Circuit breakers prevent cascading failures.
Retriable operations go through the queue system.
Graceful handling of slow or offline banks.
#### 3.4 Observability
Centralized logging with correlation IDs.
Metrics:
TPS (transactions per second)
Success/failure rates
Latency
Distributed tracing support.
#### 3.5 Security
No sensitive data stored in logs.
Secure env variables for bank credentials.
Strict input validation.
#### 3.6 Extensibility
Adding a new bank adapter should require minimal changes.
Routing engine must be pluggable.

### 4. System Design Principles Applied
#### 4.1 Separation of Concerns
API layer, routing engine, bank adapters, and workers are isolated components.
#### 4.2 Interface-Driven Development
All adapters implement the same interface to ensure pluggable integrations.
#### 4.3 Hexagonal Architecture
Domain logic is isolated from external services (banks, queues).
#### 4.4 Event-Driven Processing
Non-critical tasks flow through the queue and worker nodes.
#### 4.5 Resilience Patterns
Circuit breaker, retry, timeout, bulkhead patterns used.
#### 4.6 Single Responsibility Principle
Each service/module does exactly one job:
Routing engine → routes
Adapter → bank I/O
Worker → background processing
#### 4.7 Fail-Fast & Graceful Degradation
Quickly fail when banks are unreachable.
Return meaningful fallback responses.
#### 4.8 Cloud-Native Principles
Stateless servers
Queue-based durability
Horizontal scalability

### 5. High-Level Architecture Diagram
<img width="1017" height="641" alt="Screenshot 2025-11-15 at 10 09 34" src="https://github.com/user-attachments/assets/7ff1448e-c164-4fa9-b2fa-ff8322707a77" />




### 6. Implementation Guide (Step-by-Step)
#### Step 1 — Initialize the Project
Create a Spring Boot project and add the following pacakges.
##### Modules:
###### core/
###### adapters/
###### mock-banks/
###### workers/
infrastructure/
#### Step 2 — Define Common Data Models
##### Create models:
###### TransactionRequest
###### TransactionResponse
###### BankResponse
###### SwitchException
#### Step 3 — Build the Routing Engine
##### Map transaction types → adapter targets.
##### Add latency tracking.
##### Return unified response format.
#### Step 4 — Define the Bank Adapter Interface
public interface BankAdapter {
    BankResponse process(TransactionRequest request);
}
### Step 5 — Implement Individual Bank Adapters
#### Example:
##### BankAAdapter
##### BankBAdapter
##### BankCAdapter
##### Adapters call mock bank APIs.
### Step 6 — Implement Resilience Layer
#### Use:
##### Resilience4j (or custom)
##### CircuitBreaker
##### Retry
##### TimeLimiter
##### Wrap all adapter calls.
### Step 7 — Create Mock Bank Services With a Maker Checker
#### Local services simulating:
##### approvals
##### declines
##### timeouts
##### flaky responses
### Step 8 — Add Queue + Worker Nodes
#### Workers handle:
##### retries
##### reversals
##### settlement simulation
##### async logging

####Start with:
##### Worker1
##### Worker2
##### Worker3
### Step 9 — Add Database + Logging
#### Tables:
##### transactions
##### adapter_calls
##### errors
##### metrics
##### Optionally push raw payloads to S3-like storage.
### Step 10 — Add Analytics
#### Store metrics in analytics DB.
#### Add simple CLI or UI to query:
#### highest latency bank
#### total TPS
#### failure percentages
### Step 11 — Run End-to-End Flow
Flow:
API → Routing Engine → Bank Adapter → Resilience Layer → Response
Logging → Workers → Analytics
### 7. Future Enhancements
#### Add ISO20022 message support.
#### Add fraud scoring module.
#### Add settlement engine.
#### Deploy on AWS ECS or Kubernetes.

