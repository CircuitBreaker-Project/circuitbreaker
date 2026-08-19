# API Gateway

The API Gateway is the entry point for client requests in the Circuit Breaker project.

## Responsibilities

- Route client requests to the appropriate microservice
- Provide a single entry point for backend services
- Prepare the application for resilience and circuit breaker integration

## Gateway Configuration

The Gateway runs on:

- Port: 8080

## Service Routes

| Service | Port | Route |
|---|---:|---|
| Product Service | 8081 | `/products/**` |
| Inventory Service | 8082 | `/inventory/**` |
| Recommendation Service | 8083 | `/recommendations/**` |

## Request Flow

Client → API Gateway → Microservice

The Gateway forwards requests to the corresponding backend service based on the request path.