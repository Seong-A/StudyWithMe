package com.example.studywithme;

import java.io.Serializable;

public class Cafe implements Serializable {
    private int id;
    private String location;
    private String name;
    private String time;

    public Cafe() {
    }

    // 생성자
    public Cafe(int id, String location, String name, String time) {
        this.id = id;
        this.location = location;
        this.name = name;
        this.time = time;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
