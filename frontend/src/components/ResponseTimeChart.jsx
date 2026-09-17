import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";

import { Line } from "react-chartjs-2";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const data = {
  labels: ["1", "2", "3", "4", "5", "6", "7"],

  datasets: [
    {
      label: "Response Time (ms)",
      data: [42, 45, 41, 48, 44, 46, 43],
      tension: 0.3,
      borderWidth: 3,
    },
  ],
};

const options = {
  responsive: true,

  plugins: {
    legend: {
      position: "top",
    },

    title: {
      display: true,
      text: "Recommendation Service Response Time",
    },
  },

  scales: {
    y: {
      beginAtZero: true,
    },
  },
};

function ResponseTimeChart() {
  return (
    <div style={{ width: "700px", height: "350px" }}>
      <Line data={data} options={options} />
    </div>
  );
}

export default ResponseTimeChart;