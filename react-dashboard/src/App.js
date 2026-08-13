import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

function App() {
  const [services, setServices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

  useEffect(() => {
    fetchServices();
    const interval = setInterval(fetchServices, 10000); // Poll every 10 seconds
    return () => clearInterval(interval);
  }, []);

  const fetchServices = async () => {
    try {
      setLoading(true);
      const response = await axios.get(`${API_URL}/actuator/gateway/routes`);
      setServices(response.data || []);
      setError(null);
    } catch (err) {
      setError('Failed to fetch services. API Gateway may be unavailable.');
      console.error('Error fetching services:', err);
    } finally {
      setLoading(false);
    }
  };

  const testService = async (servicePath) => {
    try {
      await axios.get(`${API_URL}${servicePath}`);
      alert('Service test successful!');
    } catch (err) {
      alert('Service test failed. Circuit breaker may be open.');
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Circuit Breaker Dashboard</h1>
        <p>Monitor and test your microservices</p>
      </header>

      <main className="App-main">
        {error && <div className="error-message">{error}</div>}

        {loading ? (
          <div className="loading">Loading services...</div>
        ) : (
          <div className="services-grid">
            <div className="service-card" onClick={() => testService('/api/service1/hello')}>
              <h3>Microservice 1</h3>
              <p>Click to test service</p>
              <div className="status-indicator active"></div>
            </div>

            <div className="service-card" onClick={() => testService('/api/service2/hello')}>
              <h3>Microservice 2</h3>
              <p>Click to test service</p>
              <div className="status-indicator active"></div>
            </div>

            <div className="service-card">
              <h3>Eureka Server</h3>
              <p>Service Discovery</p>
              <a href="http://localhost:8761" target="_blank" rel="noopener noreferrer">
                View Dashboard
              </a>
            </div>

            <div className="service-card">
              <h3>Zipkin</h3>
              <p>Distributed Tracing</p>
              <a href="http://localhost:9411" target="_blank" rel="noopener noreferrer">
                View Traces
              </a>
            </div>
          </div>
        )}

        <div className="circuit-breaker-info">
          <h2>Circuit Breaker Status</h2>
          <p>The circuit breaker pattern protects your services from cascading failures.</p>
          <ul>
            <li><strong>CLOSED:</strong> Normal operation, requests pass through</li>
            <li><strong>OPEN:</strong> Service failing, requests blocked</li>
            <li><strong>HALF-OPEN:</strong> Testing if service has recovered</li>
          </ul>
        </div>
      </main>
    </div>
  );
}

export default App;
