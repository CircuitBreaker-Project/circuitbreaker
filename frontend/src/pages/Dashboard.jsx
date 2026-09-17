import { useState } from "react";
import ServiceCard from "../components/ServiceCard";
import CircuitStatus from "../components/CircuitStatus";
import MetricsCard from "../components/MetricsCard";
import ResponseTimeChart from "../components/ResponseTimeChart";
import LatencyButton from "../components/LatencyButton";

function Dashboard() {
  const [circuitState, setCircuitState] = useState("CLOSED");

  return (
    <div className="dashboard">

      <header className="dashboard-header">
        <div>
          <h1>CircuitBreaker Monitor</h1>
          <p>Cloud-Native E-Commerce Platform</p>
        </div>

        <div className="system-status">
          <span className="status-dot"></span>
          System Online
        </div>
      </header>

      <section>
        <h2 className="section-title">Microservices</h2>

        <div className="services-grid">
          <ServiceCard
            name="Product Service"
            status="UP"
          />

          <ServiceCard
            name="Inventory Service"
            status="UP"
          />

          <ServiceCard
            name="Recommendation Service"
            status={
    circuitState === "OPEN"
      ? "DOWN"
      : circuitState === "HALF-OPEN"
        ? "TESTING"
        : "UP"
  }
          />
        </div>
      </section>

      <section className="monitoring-section">

        <CircuitStatus
          service="Recommendation Service"
          state={circuitState}
        />

        <div className="metrics-grid">

          <MetricsCard
            title="Response Time"
            value={circuitState === "OPEN" ? "3200 ms" : "42 ms"}
          />

          <MetricsCard
            title="Failed Requests"
            value={circuitState === "OPEN" ? "47" : "0"}
          />

          <MetricsCard
            title="Total Requests"
            value="120"
          />

          <MetricsCard
            title="Fallback Calls"
            value={circuitState === "OPEN" ? "32" : "0"}
          />

        </div>
      </section>

      <section className="control-section">
        <LatencyButton setCircuitState={setCircuitState} />
      </section>

      <section className="chart-section">
        <ResponseTimeChart />
      </section>

    </div>
  );
}

export default Dashboard;