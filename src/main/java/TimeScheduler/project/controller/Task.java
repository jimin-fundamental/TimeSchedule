package TimeScheduler.project.controller;

import jakarta.persistence.*;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;
    private String name;
    private int duration;
    private boolean fixed;
    private String time;

    // Constructors, getters, and setters

    public Task(String date) {
        this.date = date;
    }

    public Task(Long id, String date, String name, int duration, boolean fixed) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.duration = duration;
        this.fixed = fixed;
    }

    public Task(Long id, String date, String name, int duration, boolean fixed, String time) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.duration = duration;
        this.fixed = fixed;
        this.time = time;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}