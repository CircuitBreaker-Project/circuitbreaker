import { useState } from "react";

function LatencyButton({ setCircuitState }) {
  const [loading, setLoading] = useState(false);

  const triggerLatency = () => {
    setLoading(true);

    // After 1.5 seconds → Circuit OPEN
    setTimeout(() => {
      setCircuitState("OPEN");
      setLoading(false);
    }, 1500);

    // After 6 seconds → HALF-OPEN
    setTimeout(() => {
      setCircuitState("HALF-OPEN");
    }, 6000);

    // After 9 seconds → CLOSED
    setTimeout(() => {
      setCircuitState("CLOSED");
    }, 9000);
  };

  return (
    <button
      className="latency-button"
      onClick={triggerLatency}
      disabled={loading}
    >
      {loading ? "Triggering Latency..." : "⚡ Trigger Latency"}
    </button>
  );
}

export default LatencyButton;