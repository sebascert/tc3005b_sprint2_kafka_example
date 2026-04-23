package com.example.consumer.repository;

import com.example.consumer.model.Patient;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class PatientRepository {

  private final Map<Long, Patient> patients = new ConcurrentHashMap<>();

  public Patient save(Patient patient) {
    patients.put(patient.getId(), patient);
    return patient;
  }

  public Patient findById(Long id) {
    return patients.get(id);
  }

  public void deleteById(Long id) {
    patients.remove(id);
  }

  public Collection<Patient> findAll() {
    return patients.values();
  }
}
