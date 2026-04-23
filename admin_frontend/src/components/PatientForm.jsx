import { useState } from "react";
import { request } from "../api/api";

export default function PatientForm({ setMessage }) {
  const [form, setForm] = useState({
    patientId: "",
    name: "",
    age: "",
  });

  async function handle(action) {
    try {
      let res;

      if (action === "create") {
        res = await request("/patients", {
          method: "POST",
          body: JSON.stringify({
            patientId: Number(form.patientId),
            name: form.name,
            age: Number(form.age),
          }),
        });
      }

      if (action === "update") {
        res = await request(`/patients/${form.patientId}`, {
          method: "PUT",
          body: JSON.stringify({
            name: form.name,
            age: Number(form.age),
          }),
        });
      }

      if (action === "delete") {
        res = await request(`/patients/${form.patientId}`, {
          method: "DELETE",
        });
      }

      setMessage(res);
    } catch (e) {
      setMessage(e.message);
    }
  }

  return (
    <div>
      <h2>Patients</h2>

      <input placeholder="ID" onChange={e => setForm({...form, patientId: e.target.value})} />
      <input placeholder="Name" onChange={e => setForm({...form, name: e.target.value})} />
      <input placeholder="Age" onChange={e => setForm({...form, age: e.target.value})} />

      <button onClick={() => handle("create")}>Register</button>
      <button onClick={() => handle("update")}>Update</button>
      <button onClick={() => handle("delete")}>Delete</button>
    </div>
  );
}
