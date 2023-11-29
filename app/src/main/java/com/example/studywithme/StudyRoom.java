package com.example.studywithme;

import java.io.Serializable;

public class StudyRoom implements Serializable {
    private int id;
    private String endUsingTime;
    private String reservationTime;
    private String status;

    public StudyRoom() {
    }

    public StudyRoom(int id, String endUsingTime, String reservationTime, String status) {
        this.id = id;
        this.endUsingTime = endUsingTime;
        this.reservationTime = reservationTime;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEndUsingTime() {
        return endUsingTime;
    }

    public void setEndUsingTime(String endUsingTime) {
        this.endUsingTime = endUsingTime;
    }

    public String getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(String reservationTime) {
        this.reservationTime = reservationTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
