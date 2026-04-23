package com.example.consumer.dto;

import com.example.consumer.model.Appointment;
import com.example.consumer.model.Patient;
import java.util.List;

public class PatientStatusResponse {
  private String responseType;
  private Patient patient;
  private List<Appointment> appointments;
  private String message;

  public PatientStatusResponse() {}

  public PatientStatusResponse(
      String responseType, Patient patient, List<Appointment> appointments, String message) {
    this.responseType = responseType;
    this.patient = patient;
    this.appointments = appointments;
    this.message = message;
  }

  public String getResponseType() {
    return responseType;
  }

  public void setResponseType(String responseType) {
    this.responseType = responseType;
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

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
