package com.example.consumer.service;

import com.example.consumer.dto.PatientStatusEvent;
import com.example.consumer.model.Appointment;
import com.example.consumer.model.Patient;
import com.example.consumer.model.PatientView;
import com.example.consumer.repository.AppointmentRepository;
import com.example.consumer.repository.PatientRepository;
import java.util.List;
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

  public void processStatusEvent(PatientStatusEvent event) {
    if (event == null || event.getEventType() == null) {
      return;
    }

    switch (event.getEventType()) {
      case "STATUS_QUERY" -> handleStatusQuery(event);
      case "HISTORY_QUERY" -> handleHistoryQuery(event);
      default -> System.out.println("Unknown status event type: " + event.getEventType());
    }
  }

  private void handleStatusQuery(PatientStatusEvent event) {
    Patient patient = patientRepository.findById(event.getPatientId());
    List<Appointment> appointments = appointmentRepository.findByPatientId(event.getPatientId());

    String summary =
        patient == null
            ? "Patient not found"
            : "Patient found. Current appointments: " + appointments.size();

    System.out.println("STATUS QUERY -> patientId=" + event.getPatientId() + " | " + summary);
  }

  private void handleHistoryQuery(PatientStatusEvent event) {
    List<Appointment> appointments = appointmentRepository.findByPatientId(event.getPatientId());
    System.out.println(
        "HISTORY QUERY -> patientId=" + event.getPatientId() + " | records=" + appointments.size());
  }

  public PatientView getPatientStatus(Long patientId) {
    Patient patient = patientRepository.findById(patientId);
    List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);

    String summary =
        patient == null
            ? "Patient not found"
            : "Patient is registered with " + appointments.size() + " appointment(s)";

    return new PatientView(patient, appointments, summary);
  }

  public List<Appointment> getPatientHistory(Long patientId) {
    return appointmentRepository.findByPatientId(patientId);
  }
}
