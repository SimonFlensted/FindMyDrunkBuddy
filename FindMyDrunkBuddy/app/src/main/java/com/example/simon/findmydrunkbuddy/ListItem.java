package com.example.simon.findmydrunkbuddy;

/**
 * Created by Simon-PC on 19-04-2017.
 */

public class ListItem {

    private int id;
    private String name;
    private int duration;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

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

    public ListItem(int id, String name, int duration, String password) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.password = password;
    }

    @Override
    public String toString() {
        return name + '\'' +
                " duration=" + duration;
    }

    public int getDuration() {

        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
