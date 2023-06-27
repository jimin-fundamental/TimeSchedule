package TimeScheduler.project.domain;

import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int duration;
    private boolean fixed;
    private LocalTime startTime;
    private LocalTime endTime;
    private int priority;

    public Task() {
    }

    public Task(String name, int duration, boolean fixed, int priority) {
        this.name = name;
        this.duration = duration;
        this.fixed = fixed;
        this.priority = priority;
    }

    public Task(Long id, String name, int duration, boolean fixed, LocalTime startTime, LocalTime endTime, int priority) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.fixed = fixed;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @JsonSetter
    public void setStartTime(String startTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        this.startTime = LocalTime.parse(startTime, formatter);
    }

    @JsonSetter
    public void setEndTime(String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        this.endTime = LocalTime.parse(endTime, formatter);
    }

    // Getters and Setters

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

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
