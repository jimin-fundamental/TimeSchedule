package TimeScheduler.project.domain;

import TimeScheduler.project.controller.Task;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "schedule_tasks")
    private List<Task> tasks = new ArrayList<>(); // Initialize the tasks list

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
