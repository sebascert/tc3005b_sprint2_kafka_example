package com.example.consumer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Appointment {

  @Id private Long appointmentId;

  private Long patientId;
  private String doctorName;
  private String dateTime;
  private String status;
  private String lastUpdated;

  public Appointment() {}

  public Appointment(
      Long appointmentId,
      Long patientId,
      String doctorName,
      String dateTime,
      String status,
      String lastUpdated) {
    this.appointmentId = appointmentId;
    this.patientId = patientId;
    this.doctorName = doctorName;
    this.dateTime = dateTime;
    this.status = status;
    this.lastUpdated = lastUpdated;
  }

  public Long getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(Long appointmentId) {
    this.appointmentId = appointmentId;
  }

  public Long getPatientId() {
    return patientId;
  }

  public void setPatientId(Long patientId) {
    this.patientId = patientId;
  }

  public String getDoctorName() {
    return doctorName;
  }

  public void setDoctorName(String doctorName) {
    this.doctorName = doctorName;
  }

  public String getDateTime() {
    return dateTime;
  }

  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(String lastUpdated) {
    this.lastUpdated = lastUpdated;
  }
}
