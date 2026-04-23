import { useState } from "react";
import PatientForm from "./components/PatientForm";
import AppointmentForm from "./components/AppointmentForm";
import "./styles/global.css";

export default function App() {
  const [message, setMessage] = useState("Ready");

  return (
    <div>
      <h1>Doctor System</h1>

      <PatientForm setMessage={setMessage} />
      <AppointmentForm setMessage={setMessage} />

      <h3>Response:</h3>
      <p>{message}</p>
    </div>
  );
}
