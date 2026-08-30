import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

function App() {
  const API_URL =
    process.env.REACT_APP_API_URL || 'http://localhost:8090';

  const [services, setServices] = useState({
    product: {
      status: 'CHECKING',
      count: 0,
      circuit: 'UNKNOWN'
    },
    inventory: {
      status: 'CHECKING',
      count: 0,
      circuit: 'UNKNOWN'
    },
    recommendation: {
      status: 'CHECKING',
      count: 0,
      circuit: 'UNKNOWN'
    }
  });

  const [gatewayStatus, setGatewayStatus] = useState('CHECKING');
  const [lastUpdated, setLastUpdated] = useState('');

  // ==========================================
  // GET CIRCUIT BREAKER STATE
  // ==========================================
  const getCircuitState = async (name) => {
    try {
      const response = await axios.get(
        `${API_URL}/actuator/metrics/resilience4j.circuitbreaker.state?tag=name:${name}`
      );

      const measurements = response.data.measurements || [];

      const states = {
        closed: 0,
        open: 0,
        half_open: 0
      };

      measurements.forEach((measurement) => {
        if (measurement.statistic === 'VALUE') {
          // State-specific queries are handled below.
        }
      });

      // Check OPEN
      const openResponse = await axios.get(
        `${API_URL}/actuator/metrics/resilience4j.circuitbreaker.state?tag=name:${name}&tag=state:open`
      );

      if (
        openResponse.data.measurements &&
        openResponse.data.measurements[0] &&
        openResponse.data.measurements[0].value === 1
      ) {
        return 'OPEN';
      }

      // Check HALF OPEN
      const halfOpenResponse = await axios.get(
        `${API_URL}/actuator/metrics/resilience4j.circuitbreaker.state?tag=name:${name}&tag=state:half_open`
      );

      if (
        halfOpenResponse.data.measurements &&
        halfOpenResponse.data.measurements[0] &&
        halfOpenResponse.data.measurements[0].value === 1
      ) {
        return 'HALF-OPEN';
      }

      // Check CLOSED
      const closedResponse = await axios.get(
        `${API_URL}/actuator/metrics/resilience4j.circuitbreaker.state?tag=name:${name}&tag=state:closed`
      );

      if (
        closedResponse.data.measurements &&
        closedResponse.data.measurements[0] &&
        closedResponse.data.measurements[0].value === 1
      ) {
        return 'CLOSED';
      }

      return 'UNKNOWN';
    } catch (error) {
      console.error(`Circuit breaker error for ${name}:`, error);
      return 'UNKNOWN';
    }
  };

  // ==========================================
  // FETCH PRODUCT SERVICE
  // ==========================================
  const fetchProducts = async () => {
    try {
      const response = await axios.get(`${API_URL}/api/products`);

      return {
        status: 'UP',
        count: Array.isArray(response.data)
          ? response.data.length
          : 0
      };
    } catch (error) {
      return {
        status: 'DOWN',
        count: 0
      };
    }
  };

  // ==========================================
  // FETCH INVENTORY SERVICE
  // ==========================================
  const fetchInventory = async () => {
    try {
      const response = await axios.get(`${API_URL}/api/inventory`);

      return {
        status: 'UP',
        count: Array.isArray(response.data)
          ? response.data.length
          : 0
      };
    } catch (error) {
      return {
        status: 'DOWN',
        count: 0
      };
    }
  };

  // ==========================================
  // FETCH RECOMMENDATION SERVICE
  // ==========================================
  const fetchRecommendations = async () => {
    try {
      const response = await axios.get(
        `${API_URL}/api/recommendations`
      );

      return {
        status: 'UP',
        count: Array.isArray(response.data)
          ? response.data.length
          : 0
      };
    } catch (error) {
      return {
        status: 'DOWN',
        count: 0
      };
    }
  };

  // ==========================================
  // FETCH ALL SERVICES
  // ==========================================
  const fetchServices = async () => {
    try {
      const gatewayHealth = await axios.get(
        `${API_URL}/actuator/health`
      );

      if (gatewayHealth.data.status === 'UP') {
        setGatewayStatus('UP');
      } else {
        setGatewayStatus('DOWN');
      }
    } catch (error) {
      setGatewayStatus('DOWN');
    }

    const product = await fetchProducts();
    const inventory = await fetchInventory();
    const recommendation = await fetchRecommendations();

    const productCircuit = await getCircuitState(
      'productCircuitBreaker'
    );

    const inventoryCircuit = await getCircuitState(
      'inventoryCircuitBreaker'
    );

    const recommendationCircuit = await getCircuitState(
      'recommendationCircuitBreaker'
    );

    setServices({
      product: {
        ...product,
        circuit: productCircuit
      },
      inventory: {
        ...inventory,
        circuit: inventoryCircuit
      },
      recommendation: {
        ...recommendation,
        circuit: recommendationCircuit
      }
    });

    setLastUpdated(new Date().toLocaleTimeString());
  };

  // ==========================================
  // TEST PRODUCT
  // ==========================================
  const testProduct = async () => {
    try {
      const response = await axios.get(
        `${API_URL}/api/products`
      );

      alert(
        `Product Service Response:\n${JSON.stringify(
          response.data,
          null,
          2
        )}`
      );

      fetchServices();
    } catch (error) {
      alert(
        'Product Service failed.\nCircuit breaker may be OPEN.'
      );

      fetchServices();
    }
  };

  // ==========================================
  // TEST INVENTORY
  // ==========================================
  const testInventory = async () => {
    try {
      const response = await axios.get(
        `${API_URL}/api/inventory`
      );

      alert(
        `Inventory Service Response:\n${JSON.stringify(
          response.data,
          null,
          2
        )}`
      );

      fetchServices();
    } catch (error) {
      alert(
        'Inventory Service failed.\nCircuit breaker may be OPEN.'
      );

      fetchServices();
    }
  };

  // ==========================================
  // TEST RECOMMENDATION
  // ==========================================
  const testRecommendation = async () => {
    try {
      const response = await axios.get(
        `${API_URL}/api/recommendations`
      );

      alert(
        `Recommendation Service Response:\n${JSON.stringify(
          response.data,
          null,
          2
        )}`
      );

      fetchServices();
    } catch (error) {
      alert(
        'Recommendation Service failed.\nCircuit breaker may be OPEN.'
      );

      fetchServices();
    }
  };

  // ==========================================
  // INITIAL LOAD + AUTO REFRESH
  // ==========================================
  useEffect(() => {
    fetchServices();

    const interval = setInterval(() => {
      fetchServices();
    }, 10000);

    return () => clearInterval(interval);
  }, []);

  // ==========================================
  // CIRCUIT BADGE
  // ==========================================
  const getCircuitClass = (state) => {
    switch (state) {
      case 'CLOSED':
        return 'circuit-closed';

      case 'OPEN':
        return 'circuit-open';

      case 'HALF-OPEN':
        return 'circuit-half-open';

      default:
        return 'circuit-unknown';
    }
  };

  // ==========================================
  // SERVICE CARD
  // ==========================================
  const ServiceCard = ({
    title,
    status,
    count,
    circuit,
    buttonText,
    onClick
  }) => {
    return (
      <div className="service-card">

        <h2>{title}</h2>

        <div className="service-status">
          <strong>Status:</strong>

          <span
            className={
              status === 'UP'
                ? 'status-up'
                : status === 'DOWN'
                ? 'status-down'
                : 'status-checking'
            }
          >
            {status}
          </span>
        </div>

        <div className="service-count">
          Available items: <strong>{count}</strong>
        </div>

        <div className="circuit-status">
          <strong>Circuit Breaker:</strong>

          <span className={getCircuitClass(circuit)}>
            {circuit}
          </span>
        </div>

        <button onClick={onClick}>
          {buttonText}
        </button>

      </div>
    );
  };

  return (
    <div className="App">

      {/* ==========================================
          HEADER
      ========================================== */}

      <header className="App-header">

        <h1>Circuit Breaker Dashboard</h1>

        <p>
          Monitor and test your microservices
        </p>

        <div className="gateway-info">
          API Gateway: {API_URL}
        </div>

        <div className="last-updated">
          Last updated: {lastUpdated || 'Loading...'}
        </div>

      </header>


      {/* ==========================================
          MAIN
      ========================================== */}

      <main className="App-main">

        {/* Gateway Status */}

        <div className="gateway-card">

          <h2>API Gateway</h2>

          <p>
            Status:{' '}

            <strong
              className={
                gatewayStatus === 'UP'
                  ? 'status-up'
                  : 'status-down'
              }
            >
              {gatewayStatus}
            </strong>
          </p>

        </div>


        {/* ==========================================
            SERVICE GRID
        ========================================== */}

        <div className="services-grid">

          <ServiceCard
            title="Product Service"
            status={services.product.status}
            count={services.product.count}
            circuit={services.product.circuit}
            buttonText="Test Product Service"
            onClick={testProduct}
          />

          <ServiceCard
            title="Inventory Service"
            status={services.inventory.status}
            count={services.inventory.count}
            circuit={services.inventory.circuit}
            buttonText="Test Inventory Service"
            onClick={testInventory}
          />

          <ServiceCard
            title="Recommendation Service"
            status={services.recommendation.status}
            count={services.recommendation.count}
            circuit={services.recommendation.circuit}
            buttonText="Test Recommendation Service"
            onClick={testRecommendation}
          />


          {/* ==========================================
              EUREKA
          ========================================== */}

          <div className="service-card">

            <h2>Eureka Server</h2>

            <p>
              Service Discovery
            </p>

            <div className="service-status">
              <strong>Status:</strong>

              <span className="status-up">
                RUNNING
              </span>
            </div>

            <a
              href="http://localhost:8761"
              target="_blank"
              rel="noopener noreferrer"
            >
              View Eureka Dashboard
            </a>

          </div>


          {/* ==========================================
              ZIPKIN
          ========================================== */}

          <div className="service-card">

            <h2>Zipkin</h2>

            <p>
              Distributed Tracing
            </p>

            <div className="service-status">

              <strong>Status:</strong>

              <span className="status-up">
                RUNNING
              </span>

            </div>

            <a
              href="http://localhost:9411"
              target="_blank"
              rel="noopener noreferrer"
            >
              View Zipkin Traces
            </a>

          </div>

        </div>


        {/* ==========================================
            CIRCUIT BREAKER INFORMATION
        ========================================== */}

        <div className="circuit-breaker-info">

          <h2>
            Circuit Breaker Status
          </h2>

          <p>
            The circuit breaker pattern protects
            your services from cascading failures.
          </p>

          <ul>

            <li>
              <strong>CLOSED:</strong>{' '}
              Normal operation, requests pass through.
            </li>

            <li>
              <strong>OPEN:</strong>{' '}
              Service failing, requests are blocked
              and fallback is returned.
            </li>

            <li>
              <strong>HALF-OPEN:</strong>{' '}
              Testing whether the service has recovered.
            </li>

          </ul>

        </div>

      </main>

    </div>
  );
}

export default App;