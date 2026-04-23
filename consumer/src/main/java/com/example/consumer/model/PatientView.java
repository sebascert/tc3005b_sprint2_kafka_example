package com.example.consumer.model;

import java.util.List;

public class PatientView {
  private Patient patient;
  private List<Appointment> appointments;
  private String summary;

  public PatientView() {}

  public PatientView(Patient patient, List<Appointment> appointments, String summary) {
    this.patient = patient;
    this.appointments = appointments;
    this.summary = summary;
  }

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public List<Appointment> getAppointments() {
    return appointments;
  }

  public void setAppointments(List<Appointment> appointments) {
    this.appointments = appointments;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }
}
