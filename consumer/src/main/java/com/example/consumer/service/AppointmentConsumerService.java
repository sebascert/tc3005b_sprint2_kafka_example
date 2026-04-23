package com.example.consumer.service;

import com.example.consumer.dto.AppointmentEvent;
import com.example.consumer.model.Appointment;
import com.example.consumer.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

@Service
public class AppointmentConsumerService {

  private final AppointmentRepository appointmentRepository;

  public AppointmentConsumerService(AppointmentRepository appointmentRepository) {
    this.appointmentRepository = appointmentRepository;
  }

  public void processAppointmentEvent(AppointmentEvent event) {
    if (event == null || event.getEventType() == null) {
      return;
    }

    switch (event.getEventType()) {
      case "CREATE" -> create(event);
      case "CANCEL" -> cancel(event);
      case "RESCHEDULE" -> reschedule(event);
      default -> System.out.println("Unknown appointment event type: " + event.getEventType());
    }
  }

  private void create(AppointmentEvent event) {
    Appointment appointment =
        new Appointment(
            event.getAppointmentId(),
            event.getPatientId(),
            event.getDoctorName(),
            event.getDateTime(),
            "CREATED",
            event.getTimestamp());

    appointmentRepository.save(appointment);
    System.out.println("Appointment created: " + event.getAppointmentId());
  }

  private void cancel(AppointmentEvent event) {
    Appointment existing = appointmentRepository.findById(event.getAppointmentId());
    if (existing == null) {
      existing = new Appointment();
      existing.setAppointmentId(event.getAppointmentId());
      existing.setPatientId(event.getPatientId());
      existing.setDoctorName(event.getDoctorName());
      existing.setDateTime(event.getDateTime());
    }

    existing.setStatus("CANCELLED");
    existing.setLastUpdated(event.getTimestamp());
    appointmentRepository.save(existing);

    System.out.println("Appointment cancelled: " + event.getAppointmentId());
  }

  private void reschedule(AppointmentEvent event) {
    Appointment existing = appointmentRepository.findById(event.getAppointmentId());
    if (existing == null) {
      existing = new Appointment();
      existing.setAppointmentId(event.getAppointmentId());
      existing.setPatientId(event.getPatientId());
    }

    existing.setDoctorName(event.getDoctorName());
    existing.setDateTime(event.getDateTime());
    existing.setStatus("RESCHEDULED");
    existing.setLastUpdated(event.getTimestamp());
    appointmentRepository.save(existing);

    System.out.println("Appointment rescheduled: " + event.getAppointmentId());
  }
}
