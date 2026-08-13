import { FaCircleCheck } from "react-icons/fa6";
function ServiceCard({ name, status }) {
  const isUp = status === "UP";

  return (
    <div className="service-card">
      <div>
        <h3>{name}</h3>
        <p>Spring Boot Microservice</p>
      </div>

      <div className={`service-status ${isUp ? "up" : "down"}`}>
        <span className="status-dot"></span>
        {status}
      </div>
    </div>
  );
}

export default ServiceCard;