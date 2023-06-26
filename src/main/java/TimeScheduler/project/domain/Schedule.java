package TimeScheduler.project.domain;

import TimeScheduler.project.controller.Task;
import jakarta.persistence.*;

import java.util.List;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;

    @ElementCollection
    @CollectionTable(name = "schedule_tasks")
    private List<Task> tasks;

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}





