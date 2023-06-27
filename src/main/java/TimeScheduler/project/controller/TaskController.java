package TimeScheduler.project.controller;

import TimeScheduler.project.domain.Schedule;
import com.fasterxml.jackson.databind.ObjectMapper; // for JSON conversion
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    private final Schedule schedule;

    // Use constructor injection for the schedule
    public TaskController(Schedule schedule) {
        this.schedule = schedule;
    }

    @GetMapping("/tasks")
    public String getTasks() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(schedule.getTasks());
    }
}

