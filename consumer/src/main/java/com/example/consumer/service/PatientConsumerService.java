package com.example.consumer.service;

import com.example.consumer.dto.PatientEvent;
import com.example.consumer.model.Patient;
import com.example.consumer.repository.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientConsumerService {

  private final PatientRepository patientRepository;

  public PatientConsumerService(PatientRepository patientRepository) {
    this.patientRepository = patientRepository;
  }

  public void processPatientEvent(PatientEvent event) {
    if (event == null || event.getEventType() == null) {
      return;
    }

    switch (event.getEventType()) {
      case "REGISTER" -> register(event);
      case "UPDATE" -> update(event);
      case "DELETE" -> delete(event.getPatientId());
      default -> System.out.println("Unknown patient event type: " + event.getEventType());
    }
  }

  private void register(PatientEvent event) {
    Patient patient =
        new Patient(event.getPatientId(), event.getName(), event.getAge(), event.getTimestamp());
    patientRepository.save(patient);
    System.out.println("Patient registered: " + event.getPatientId());
  }

  private void update(PatientEvent event) {
    Patient existing = patientRepository.findById(event.getPatientId());
    if (existing == null) {
      existing = new Patient();
      existing.setId(event.getPatientId());
    }

    existing.setName(event.getName());
    existing.setAge(event.getAge());
    existing.setLastUpdated(event.getTimestamp());

    patientRepository.save(existing);
    System.out.println("Patient updated: " + event.getPatientId());
  }

  private void delete(Long patientId) {
    patientRepository.deleteById(patientId);
    System.out.println("Patient deleted: " + patientId);
  }
}
