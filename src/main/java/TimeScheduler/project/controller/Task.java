package TimeScheduler.project.controller;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date = "27";
    private String name;
    private int duration;
    private boolean fixed;
    private LocalTime startTime;
    private LocalTime endTime;
    private int priority;

    // Constructors, getters, and setters

    public Task() {
    }

    public Task(String date) {
        this.date = date;
    }

    public Task(Long id, String date, String name, int duration, boolean fixed, int priority) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.duration = duration;
        this.fixed = fixed;
        this.priority = priority;
    }

    public Task(Long id, String date, String name, int duration, boolean fixed, LocalTime startTime, LocalTime endTime, int priority) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.duration = duration;
        this.fixed = fixed;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
    }
    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
