# Circuit Breaker Project - Infrastructure

This directory contains the complete infrastructure setup for the Circuit Breaker Project, including Docker configurations, service discovery, and distributed tracing.

## 🏗️ Infrastructure Components

### Services
- **Eureka Server** (Port 8761) - Service Discovery
- **API Gateway** (Port 8080) - Gateway with Circuit Breaker Pattern
- **Microservice 1** (Port 8081) - Sample microservice
- **Microservice 2** (Port 8082) - Sample microservice
- **React Dashboard** (Port 3000) - Monitoring dashboard
- **Zipkin** (Port 9411) - Distributed tracing

## 🚀 Quick Start

### Prerequisites
- Docker installed
- Docker Compose installed
- Git installed

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/CircuitBreaker-Project/circuitbreaker.git
   cd circuitbreaker
   git checkout feature/infrastructure
   ```

2. **Configure environment variables**
   ```bash
   # The .env file is already configured with default values
   # Modify if needed for your environment
   ```

3. **Build and start all services**
   ```bash
   docker-compose up -d
   ```

4. **Verify services are running**
   ```bash
   docker-compose ps
   ```

5. **Access the services**
   - Eureka Dashboard: http://localhost:8761
   - API Gateway: http://localhost:8080
   - React Dashboard: http://localhost:3000
   - Zipkin Tracing: http://localhost:9411

## 📋 Service Details

### Eureka Server
- **Purpose**: Service discovery and registration
- **Technology**: Spring Cloud Eureka
- **Health Check**: http://localhost:8761/actuator/health

### API Gateway
- **Purpose**: API routing and circuit breaker implementation
- **Technology**: Spring Cloud Gateway + Resilience4j
- **Features**: 
  - Service discovery integration
  - Circuit breaker pattern
  - Distributed tracing
- **Health Check**: http://localhost:8080/actuator/health

### Microservices
- **Purpose**: Sample services demonstrating circuit breaker usage
- **Technology**: Spring Boot + Resilience4j
- **Features**:
  - Eureka client registration
  - Circuit breaker configuration
  - Zipkin tracing
- **Health Checks**: 
  - Microservice 1: http://localhost:8081/actuator/health
  - Microservice 2: http://localhost:8082/actuator/health

### React Dashboard
- **Purpose**: Visual monitoring interface
- **Technology**: React + Nginx
- **Features**:
  - Service status monitoring
  - Circuit breaker status display
  - Service testing interface

### Zipkin
- **Purpose**: Distributed tracing and monitoring
- **Technology**: Zipkin Server
- **Features**:
  - Request tracing across services
  - Performance monitoring
  - Dependency analysis

## 🔧 Configuration

### Circuit Breaker Settings
Circuit breaker configuration in `.env`:
- `CIRCUIT_BREAKER_FAILURE_RATE_THRESHOLD=50` - Failure rate percentage to trip
- `CIRCUIT_BREAKER_WAIT_DURATION_IN_OPEN_STATE=10s` - Time before attempting recovery
- `CIRCUIT_BREAKER_SLIDING_WINDOW_SIZE=10` - Number of calls to evaluate
- `CIRCUIT_BREAKER_MINIMUM_NUMBER_OF_CALLS=5` - Minimum calls before evaluation
- `CIRCUIT_BREAKER_PERMITTED_NUMBER_OF_CALLS_IN_HALF_OPEN_STATE=3` - Test calls in half-open state

### Network Configuration
- **Network Name**: circuit-breaker-network
- **Subnet**: 172.20.0.0/16
- **Driver**: bridge

## 🛠️ Management Commands

### Start all services
```bash
docker-compose up -d
```

### Stop all services
```bash
docker-compose down
```

### View logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f eureka-server
docker-compose logs -f api-gateway
```

### Restart specific service
```bash
docker-compose restart api-gateway
```

### Rebuild specific service
```bash
docker-compose up -d --build api-gateway
```

### Scale services
```bash
docker-compose up -d --scale microservice-1=3
```

## 📊 Monitoring

### Service Health
- **Eureka**: Check service registration at http://localhost:8761
- **Gateway**: Monitor circuit breaker status at http://localhost:8080/actuator/circuitbreakers
- **Zipkin**: View distributed traces at http://localhost:9411

### Circuit Breaker States
1. **CLOSED**: Normal operation, requests pass through
2. **OPEN**: Service failing, requests blocked immediately
3. **HALF-OPEN**: Testing if service has recovered

## 🔍 Troubleshooting

### Services not starting
```bash
# Check logs
docker-compose logs

# Check resource usage
docker stats

# Rebuild containers
docker-compose down
docker-compose up -d --build
```

### Services not discovering each other
- Verify Eureka server is running: http://localhost:8761
- Check network connectivity: `docker network inspect circuit-breaker-network`
- Verify environment variables in `.env`

### Circuit breaker not working
- Check Resilience4j configuration in `api-gateway/src/main/resources/application.yml`
- Verify circuit breaker metrics: http://localhost:8080/actuator/circuitbreakers
- Check logs for circuit breaker events

## 📝 Architecture

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   React     │────▶│   Gateway   │────▶│  Service 1  │
│  Dashboard  │     │             │     │             │
└─────────────┘     └─────────────┘     └─────────────┘
                          │                     │
                          │             ┌───────┴───────┐
                          │             │               │
                   ┌──────┴──────┐ ┌────▼────┐    ┌────▼────┐
                   │   Eureka    │ │Service 2│    │  Zipkin │
                   │   Server    │ └─────────┘    └─────────┘
                   └─────────────┘
```

## 🤝 Contributing

This is the infrastructure branch. For contributions:
1. Make changes to infrastructure files
2. Test locally with Docker Compose
3. Commit changes to `feature/infrastructure` branch
4. Create pull request to main branch

## 📄 License

This project is part of the Circuit Breaker Project team assignment.

## 👥 Team Structure

- **Member 1** - Gateway & Resilience
- **Member 2** - Microservices  
- **Member 3** - React Dashboard
- **Member 4** - Infrastructure (This branch)

---

**Note**: This infrastructure setup provides the foundation for the circuit breaker pattern implementation. Ensure all team members coordinate their respective module integration with this infrastructure.
