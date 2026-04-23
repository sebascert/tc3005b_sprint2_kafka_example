package com.example.consumer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Patient {

  @Id private Long id;

  private String name;
  private Integer age;
  private String lastUpdated;

  public Patient() {}

  public Patient(Long id, String name, Integer age, String lastUpdated) {
    this.id = id;
    this.name = name;
    this.age = age;
    this.lastUpdated = lastUpdated;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(String lastUpdated) {
    this.lastUpdated = lastUpdated;
  }
}
