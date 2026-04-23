package com.example.producer.dto;

import java.util.List;
import java.util.Map;

public class PatientStatusResponse {
  private String responseType;
  private Map<String, Object> patient;
  private List<Map<String, Object>> appointments;
  private String message;

  public PatientStatusResponse() {}

  public String getResponseType() {
    return responseType;
  }

  public void setResponseType(String responseType) {
    this.responseType = responseType;
  }

  public Map<String, Object> getPatient() {
    return patient;
  }

  public void setPatient(Map<String, Object> patient) {
    this.patient = patient;
  }

  public List<Map<String, Object>> getAppointments() {
    return appointments;
  }

  public void setAppointments(List<Map<String, Object>> appointments) {
    this.appointments = appointments;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
