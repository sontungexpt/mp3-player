package com.example.calllog;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "call_log")
public class CallLogEntity {
  @PrimaryKey(autoGenerate = true)
  private int id;

  private String number;
  private String type;
  private long date;
  private String duration;

  // Getters và Setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public long getDate() {
    return date;
  }

  public void setDate(long date) {
    this.date = date;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }
}
