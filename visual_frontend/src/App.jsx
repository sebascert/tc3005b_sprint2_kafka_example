import VisualizationSystem from "./components/VisualizationSystem";
import "./styles/global.css";

export default function App() {
  return (
    <main className="app">
      <header className="hero">
        <h1>Doctor System</h1>
        <p>
          Interface for patient status visualization and appointment history
          queries through the producer backend.
        </p>
      </header>

      <VisualizationSystem />
    </main>
  );
}
