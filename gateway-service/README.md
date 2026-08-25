# API Gateway

The API Gateway is the central entry point for client requests in the CircuitBreaker cloud-native e-commerce application.

It uses **Spring Cloud Gateway**, **Netflix Eureka Service Discovery**, and **Resilience4j** to provide dynamic routing and fault tolerance for backend microservices.

## Technology Stack

* Java 17
* Spring Boot 3.5.3
* Spring Cloud 2025.0.0
* Spring Cloud Gateway
* Netflix Eureka Client
* Spring Cloud LoadBalancer
* Resilience4j
* Spring Boot Actuator
* Maven

## Gateway Configuration

| Configuration    | Value                           |
| ---------------- | ------------------------------- |
| Application Name | `api-gateway`                   |
| Port             | `8080`                          |
| Eureka Server    | `http://localhost:8761/eureka/` |

## Service Routes

The Gateway uses service discovery and load balancing to route requests using `lb://` service identifiers.

| Service                | Service ID               | Route                 |
| ---------------------- | ------------------------ | --------------------- |
| Product Service        | `product-service`        | `/products/**`        |
| Inventory Service      | `inventory-service`      | `/inventory/**`       |
| Recommendation Service | `recommendation-service` | `/recommendations/**` |

### Product Route

```text
/products/**
      ↓
lb://product-service
```

### Inventory Route

```text
/inventory/**
      ↓
lb://inventory-service
```

### Recommendation Route

```text
/recommendations/**
      ↓
lb://recommendation-service
      ↓
Resilience4j Circuit Breaker
```

## Eureka Service Discovery

The Gateway is configured as a Eureka client.

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: true
    fetch-registry: true
```

The Gateway discovers backend services dynamically instead of using fixed backend URLs.

When the application is run directly on the local machine, Eureka is expected at:

```text
http://localhost:8761/eureka/
```

When the complete application is later run through Docker Compose, the Eureka address will use the Docker service name.

## Week 1 Implementation

The Week 1 Gateway implementation provides:

* Spring Cloud Gateway setup
* Dynamic service routing
* Eureka client integration
* Spring Cloud LoadBalancer integration
* Product Service routing
* Inventory Service routing
* Recommendation Service routing
* Gateway running on port `8080`

Request flow:

```text
Client
   ↓
API Gateway :8080
   ↓
Eureka Service Discovery
   ↓
Requested Microservice
```

## Week 2 Resilience Implementation

The Recommendation Service route is protected using Resilience4j.

Circuit breaker name:

```text
recommendationServiceCircuitBreaker
```

### Circuit Breaker Configuration

| Setting                        |       Value |
| ------------------------------ | ----------: |
| Sliding Window Type            | COUNT_BASED |
| Sliding Window Size            |    10 calls |
| Minimum Calls                  |           5 |
| Failure Rate Threshold         |         50% |
| Open State Duration            |  10 seconds |
| Half-Open Test Calls           |           3 |
| Automatic Half-Open Transition |     Enabled |
| Request Timeout                |   3 seconds |

### Circuit Breaker Flow

```text
Client
   ↓
API Gateway
   ↓
Recommendation Service
   ↓
Failure / Timeout
   ↓
Circuit Breaker
   ↓
Fallback
```

When the configured failure threshold is reached, the circuit can move to the OPEN state and prevent additional requests from unnecessarily reaching the failing downstream service.

After the configured open-state duration, the circuit can transition to HALF-OPEN and allow limited test requests.

If the downstream service recovers, the circuit returns to CLOSED.

## Recommendation Fallback

The Gateway provides:

```text
/fallback/recommendation
```

When Recommendation Service is unavailable, the Gateway returns fallback data instead of leaving the client waiting for a failed downstream request.

Example response:

```json
{
  "service": "recommendation-service",
  "status": "fallback",
  "message": "Recommendation service is temporarily unavailable. Showing top sellers instead.",
  "recommendations": [
    "Laptop",
    "Wireless Headphones",
    "Smartphone",
    "Mechanical Keyboard"
  ]
}
```

This demonstrates the primary resilience goal of the project: the application can continue serving useful data even when the Recommendation Service is unavailable.

## Actuator and Monitoring

The Gateway exposes the following Actuator endpoints:

```text
/actuator/health
/actuator/info
/actuator/metrics
/actuator/prometheus
```

Circuit breaker health indicators are enabled.

Resilience4j metrics are also available through the Actuator metrics endpoint.

Important metric families include:

```text
resilience4j.circuitbreaker.calls
resilience4j.circuitbreaker.failed.calls
resilience4j.circuitbreaker.not.permitted.calls
resilience4j.circuitbreaker.state
resilience4j.circuitbreaker.slow.calls
resilience4j.circuitbreaker.slow.call.rate
```

## Verified Tests

### Gateway Health

```powershell
Invoke-WebRequest http://localhost:8080/actuator/health -UseBasicParsing
```

Expected result:

```text
HTTP 200 OK
status: UP
```

### Gateway Metrics

```powershell
Invoke-WebRequest http://localhost:8080/actuator/metrics -UseBasicParsing
```

The response confirms that Resilience4j circuit breaker metrics are registered.

### Fallback Endpoint

```powershell
Invoke-WebRequest http://localhost:8080/fallback/recommendation -UseBasicParsing
```

Expected result:

```text
HTTP 200 OK
```

with the fallback recommendation response.

### Recommendation Gateway Route

```powershell
Invoke-WebRequest http://localhost:8080/recommendations -UseBasicParsing
```

When the Recommendation Service is unavailable, the Gateway successfully returned:

```text
HTTP 200 OK
status: fallback
```

with the Top Sellers fallback data.

## Current Architecture

```text
                         Eureka Server
                            :8761
                               │
             ┌─────────────────┼─────────────────┐
             │                 │                 │
             ▼                 ▼                 ▼
      Product Service   Inventory Service   Recommendation Service
           :8081              :8082                 :8083
             │                 │                     │
             └─────────────────┼─────────────────────┘
                               │
                               ▼
                       API Gateway :8080
                               │
                     ┌─────────┴─────────┐
                     │                   │
                Routing          Resilience4j
                                         │
                                  Circuit Breaker
                                         │
                                      Fallback
```

## Current Status

### Week 1

* [x] Gateway scaffolding
* [x] Product route
* [x] Inventory route
* [x] Recommendation route
* [x] Eureka client configuration
* [x] Load balancing configuration

### Week 2

* [x] Recommendation Circuit Breaker
* [x] Failure threshold configuration
* [x] Timeout configuration
* [x] Half-open configuration
* [x] Recommendation fallback
* [x] Actuator health and metrics
* [x] Fallback verification
* [x] Gateway fallback route verification

### Integration Pending

The complete end-to-end chaos test requires the shared Recommendation Service and Eureka infrastructure to be running.

## Running the Gateway

From the `gateway-service` directory:

```powershell
mvn clean package
```

To start the Gateway:

```powershell
mvn spring-boot:run
```

The Gateway will be available at:

```text
http://localhost:8080
```
