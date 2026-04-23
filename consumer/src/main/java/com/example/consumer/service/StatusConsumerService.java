package com.example.consumer.service;

import com.example.consumer.dto.PatientStatusEvent;
import com.example.consumer.dto.PatientStatusResponse;
import com.example.consumer.model.Appointment;
import com.example.consumer.model.Patient;
import com.example.consumer.repository.AppointmentRepository;
import com.example.consumer.repository.PatientRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class StatusConsumerService {

  private final PatientRepository patientRepository;
  private final AppointmentRepository appointmentRepository;

  public StatusConsumerService(
      PatientRepository patientRepository, AppointmentRepository appointmentRepository) {
    this.patientRepository = patientRepository;
    this.appointmentRepository = appointmentRepository;
  }

  public PatientStatusResponse processStatusEvent(PatientStatusEvent event) {
    if (event == null || event.getEventType() == null) {
      return new PatientStatusResponse("ERROR", null, null, "Invalid event");
    }
    return switch (event.getEventType()) {
      case "STATUS_QUERY" -> handleStatusQuery(event.getPatientId());
      case "HISTORY_QUERY" -> handleHistoryQuery(event.getPatientId());
      default ->
          new PatientStatusResponse(
              "ERROR", null, null, "Unknown event type: " + event.getEventType());
    };
  }

  private PatientStatusResponse handleStatusQuery(Long patientId) {
    Optional<Patient> opt = patientRepository.findById(patientId);
    List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);

    if (opt.isEmpty()) {
      return new PatientStatusResponse(
          "STATUS_RESPONSE", null, null, "Patient not found for id: " + patientId);
    }

    Patient p = opt.get();
    String msg =
        "Patient: "
            + p.getName()
            + ", Age: "
            + p.getAge()
            + ", Appointments: "
            + appointments.size();
    return new PatientStatusResponse("STATUS_RESPONSE", p, appointments, msg);
  }

  private PatientStatusResponse handleHistoryQuery(Long patientId) {
    Optional<Patient> opt = patientRepository.findById(patientId);
    List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);

    Patient p = opt.orElse(null);
    String msg =
        p == null
            ? "Patient not found, but found " + appointments.size() + " appointment record(s)"
            : "History for " + p.getName() + ": " + appointments.size() + " appointment(s)";
    return new PatientStatusResponse("HISTORY_RESPONSE", p, appointments, msg);
  }
}
