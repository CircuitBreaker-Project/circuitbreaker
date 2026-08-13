import { FaCircleCheck } from "react-icons/fa6";
function CircuitStatus({ service, state }) {
  const stateClass = state.toLowerCase();

  return (
    <div className={`circuit-card ${stateClass}`}>
      <div>
        <p>Circuit Breaker</p>
        <h2>{service}</h2>
      </div>

      <div className="circuit-state">
        <span className="state-dot"></span>
        <strong>{state}</strong>
      </div>

      <p>
        Circuit breaker is currently {state.toLowerCase()}.
      </p>
    </div>
  );
}

export default CircuitStatus;