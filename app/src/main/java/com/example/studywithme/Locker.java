package com.example.studywithme;

import java.io.Serializable;

public class Locker implements Serializable {
    private int lockerNumber;
    private String endUsingTime;
    private String reservationTime;
    private String status;

    public Locker() {
    }

    public Locker(int lockerNumber, String endUsingTime, String reservationTime, String status) {
        this.lockerNumber = lockerNumber;
        this.endUsingTime = endUsingTime;
        this.reservationTime = reservationTime;
        this.status = status;
    }

    public int getLockerNumber() {
        return lockerNumber;
    }

    public void setLockerNumber(int lockerNumber) {
        this.lockerNumber = lockerNumber;
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
