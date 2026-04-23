import { useState } from "react";
import { request } from "../api/api";

export default function AppointmentForm({ setMessage }) {
  const [form, setForm] = useState({
    appointmentId: "",
    patientId: "",
    doctorName: "",
    dateTime: "",
  });

  async function handle(action) {
    try {
      let res;

      if (action === "create") {
        res = await request("/appointments", {
          method: "POST",
          body: JSON.stringify({
            appointmentId: Number(form.appointmentId),
            patientId: Number(form.patientId),
            doctorName: form.doctorName,
            dateTime: form.dateTime,
          }),
        });
      }

      if (action === "reschedule") {
        res = await request(`/appointments/${form.appointmentId}/reschedule`, {
          method: "PUT",
          body: JSON.stringify({
            patientId: Number(form.patientId),
            doctorName: form.doctorName,
            dateTime: form.dateTime,
          }),
        });
      }

      if (action === "cancel") {
        res = await request(`/appointments/${form.appointmentId}`, {
          method: "DELETE",
          body: JSON.stringify({
            patientId: Number(form.patientId),
          }),
        });
      }

      setMessage(res);
    } catch (e) {
      setMessage(e.message);
    }
  }

  return (
    <div>
      <h2>Appointments</h2>

      <input placeholder="Appointment ID" onChange={e => setForm({...form, appointmentId: e.target.value})} />
      <input placeholder="Patient ID" onChange={e => setForm({...form, patientId: e.target.value})} />
      <input placeholder="Doctor Name" onChange={e => setForm({...form, doctorName: e.target.value})} />
      <input placeholder="DateTime" onChange={e => setForm({...form, dateTime: e.target.value})} />

      <button onClick={() => handle("create")}>Create</button>
      <button onClick={() => handle("reschedule")}>Reschedule</button>
      <button onClick={() => handle("cancel")}>Cancel</button>
    </div>
  );
}
