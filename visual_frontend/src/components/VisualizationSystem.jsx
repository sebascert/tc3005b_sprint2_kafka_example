import { useState } from "react";
import { request } from "../api/api";

export default function VisualizationSystem() {
  const [patientId, setPatientId] = useState("");
  const [loading, setLoading] = useState(false);
  const [response, setResponse] = useState(null);
  const [title, setTitle] = useState("Ready");

  async function handleQuery(type) {
    if (!patientId) {
      setResponse({ error: "Please enter a patient ID" });
      return;
    }

    setLoading(true);

    try {
      const path =
        type === "status"
          ? `/patients/${patientId}/status`
          : `/patients/${patientId}/history`;

      const data = await request(path, { method: "GET" });

      setTitle(type === "status" ? "Patient Status" : "Patient History");
      setResponse(data);
    } catch (error) {
      setTitle("Error");
      setResponse({ error: error.message });
    } finally {
      setLoading(false);
    }
  }

  return (
    <section className="card">
      <div className="card-header">
        <div>
          <p className="eyebrow">Sistema de visualización</p>
          <h2>Consulta de pacientes</h2>
        </div>
      </div>

      <div className="form-row">
        <input
          type="number"
          placeholder="Patient ID"
          value={patientId}
          onChange={(e) => setPatientId(e.target.value)}
        />

        <button onClick={() => handleQuery("status")} disabled={loading}>
          Visualizar estado
        </button>

        <button onClick={() => handleQuery("history")} disabled={loading}>
          Consultar citas
        </button>
      </div>

      <div className="response-panel">
        <h3>{loading ? "Loading..." : title}</h3>

        {response && (
          <pre>{JSON.stringify(response, null, 2)}</pre>
        )}
      </div>
    </section>
  );
}
