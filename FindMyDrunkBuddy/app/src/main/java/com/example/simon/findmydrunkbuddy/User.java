package com.example.simon.findmydrunkbuddy;

import java.io.Serializable;

/**
 * Created by Simon on 25-04-2017.
 */

public class User implements Serializable {
    private int id;
    private String name;
    private float lattitude;
    private float longtitude;

    public User(int id, String name, float lattitude, float longtitude) {
        this.id = id;
        this.name = name;
        this.lattitude = lattitude;
        this.longtitude = longtitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLattitude() {
        return lattitude;
    }

    public void setLattitude(float lattitude) {
        this.lattitude = lattitude;
    }

    public float getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
    }

    @Override
    public String toString() {
        return name;
    }
}
