package com.example.studywithme;

public class Reservation {
    private String email;
    private String date;
    private String fee;
    private String time;

    public Reservation() {

    }

    public Reservation(String email, String date, String fee, String time) {
        this.email = email;
        this.date = date;
        this.fee = fee;
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public String getDate() { return date; }

    public String getFee() {
        return fee;
    }

    public String getTime() {
        return time;
    }
}
