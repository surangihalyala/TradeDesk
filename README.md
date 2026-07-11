# TradeDesk

A simulated brokerage order-management and settlement platform — backend infrastructure modeling how a brokerage's trading desk manages client stock/bond orders from placement through legal settlement.

## The scenario

TradeDesk simulates the backend behind a brokerage's trading desk — internal infrastructure a bank runs to manage client stock/bond orders from placement to legal settlement.

**Users (all internal staff, not the public):**
- **Traders / front-office staff** — place buy/sell orders via `order-service`'s REST API
- **Operations/Settlements team** — rely on `settlement-service` to confirm trades clear and settle (real trades take 1-2 days to formally settle after execution)
- **Risk team** — every order passes a risk check before acceptance, flagging exposure-limit breaches
- **Compliance/Audit** — an AOP logging aspect creates an audit trail of every order state change (legally required at financial firms)
- **A legacy internal system** — the SOAP "Account Verification" service represents an old system nobody's allowed to rewrite, a common constraint at large banks

**Why the architecture looks the way it does:**
- **Microservices, not a monolith** — order placement and settlement have different scaling/failure characteristics; settlement being slow shouldn't ever block new order placement
- **Kafka between order-service and settlement-service** — order placement must respond in milliseconds; settlement can take longer. Kafka lets order-service fire an event and move on, while settlement-service processes it independently
- **JMS/queue for notifications** — guarantees delivery of "trade settled" notifications even if the notification service is briefly down when the event fires
- **Spring Batch nightly reconciliation** — real financial systems run end-of-day batch jobs comparing expected vs. actual settlement, catching discrepancies before they become accounting/regulatory problems
- **Multi-threaded risk-check with a Semaphore** — models a real constraint: many orders to risk-check at once, but a downstream dependency (external limits-checking service) that can only handle a limited number of concurrent requests

## Tech stack

| Area | Where it's covered |
|---|---|
| Core Java 17, multi-threading, Concurrency, Semaphore | Risk-check component: `ExecutorService` + `Semaphore`-capped concurrent calls to a simulated risk API |
| Spring Core, IOC, AOP | Standard DI throughout; AOP aspect for audit logging on order state changes |
| Spring Boot, Microservices | `order-service`, `settlement-service`, `notification-service` |
| Web Services (SOAP *and* REST) | REST for the order API; SOAP for the legacy "Account Verification" service (via Spring-WS) |
| Kafka | `OrderPlaced` event: order-service publishes, settlement-service consumes |
| JMS / IBM MQ | settlement-service → notification-service via JMS (ActiveMQ/Artemis locally — same JMS API as IBM MQ) |
| SQL Server, stored procedures | Real stored procedures called via `JdbcTemplate` (deliberately not full JPA/Hibernate, to keep raw SQL front and center) |
| Spring Batch | Nightly reconciliation job (orders vs. settlements), chunk-based |
| Git, Maven, JUnit, CI/CD | Repo + Maven build + GitHub Actions pipeline |
| Azure | One service deployed to Azure App Service and/or Azure SQL Database |
| Docker / Unix | Docker containers (SQL Server, etc.) run Linux; shell scripts for build/deploy |

## Build order

1. **`order-service`**: REST API + JDBC + a real stored procedure + an AOP logging aspect *(current phase)*
2. Multi-threaded risk-check component (ExecutorService + Semaphore)
3. SOAP account-verification endpoint (Spring-WS)
4. Split off `settlement-service`, wire Kafka between the two
5. JMS notification flow (settlement-service → notification-service)
6. Spring Batch nightly reconciliation job
7. CI/CD pipeline (GitHub Actions) + Azure deployment

## Current progress

- [x] Repo created and connected to GitHub (`https://github.com/surangihalyala/TradeDesk.git`)
- [x] `order-service` scaffolded via Spring Initializr:
  - Maven, Java 17, Spring Boot **4.1.0**
  - Group: `com.tradedesk`, Artifact: `order-service`
  - Dependencies: Spring Web (MVC), JDBC API, MS SQL Server Driver, Spring AOP (`spring-boot-starter-aspectj`), Validation, Lombok
- [x] `Order` domain model (`Order`, `OrderSide`, `OrderStatus`)
- [ ] SQL Server (Docker) + orders table + stored procedure
- [ ] REST endpoints + JDBC wiring
- [ ] AOP audit-logging aspect
- [ ] Everything else in the build order above
