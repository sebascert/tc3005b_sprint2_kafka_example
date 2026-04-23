package com.example.consumer.repository;

import com.example.consumer.model.Appointment;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class AppointmentRepository {

  private final Map<Long, Appointment> appointments = new ConcurrentHashMap<>();

  public Appointment save(Appointment appointment) {
    appointments.put(appointment.getAppointmentId(), appointment);
    return appointment;
  }

  public Appointment findById(Long id) {
    return appointments.get(id);
  }

  public List<Appointment> findByPatientId(Long patientId) {
    List<Appointment> result = new ArrayList<>();
    for (Appointment appointment : appointments.values()) {
      if (appointment.getPatientId() != null && appointment.getPatientId().equals(patientId)) {
        result.add(appointment);
      }
    }
    return result;
  }

  public List<Appointment> findAll() {
    return new ArrayList<>(appointments.values());
  }
}
