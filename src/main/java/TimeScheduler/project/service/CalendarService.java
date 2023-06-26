package TimeScheduler.project.service;

import TimeScheduler.project.controller.Task;
import TimeScheduler.project.domain.Schedule;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CalendarService {

    private OpenAiService openAi;

    public CalendarService(OpenAiService openAi) {
        this.openAi = openAi;
    }

    @Transactional
    public Schedule createSchedule(List<Task> tasks, String openAPIKey) throws IOException {
        Schedule schedule = new Schedule();

        // Filter tasks by fixed property
        List<Task> fixedTasks = new ArrayList<>();
        List<Task> flexibleTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isFixed()) {
                fixedTasks.add(task);
            } else {
                flexibleTasks.add(task);
            }
        }

        // Sort fixedTasks by time
        fixedTasks.sort((t1, t2) -> t1.getTime().compareTo(t2.getTime()));

        // Create schedule date
//        String scheduleDate = determineScheduleDate(); // 날짜 받아오기
        schedule.setDate("25");

        // Generate schedule for fixed tasks
        for (Task task : fixedTasks) {
            assignFixedTask(schedule, task);
        }

        // Use OpenAPI to fetch updated daily schedule
        List<Task> updatedFlexibleTasks = openAi.fetchUpdatedSchedule(flexibleTasks);

        // Generate schedule for flexible tasks
        if (!updatedFlexibleTasks.isEmpty()) {
            LocalTime startTime = LocalTime.of(7, 0); // Start time for the schedule
            LocalTime endTime = LocalTime.of(22, 0); // End time for the schedule

            List<Task> scheduledTasks = schedule.getTasks();
            for (Task task : updatedFlexibleTasks) {
                boolean assigned = false;
                for (LocalTime time = startTime; time.isBefore(endTime) && !assigned; time = time.plusMinutes(task.getDuration())) {
                    if (isTimeAvailable(time, task.getDuration(), scheduledTasks)) {
                        assignTask(schedule, task, time);
                        assigned = true;
                    }
                }
            }
        }

        return schedule;
    }

    private void assignFixedTask(Schedule schedule, Task task) {
        List<Task> tasks = schedule.getTasks();
        tasks.add(task);
        schedule.setTasks(tasks);
    }

    private void assignTask(Schedule schedule, Task task, LocalTime time) {
        List<Task> tasks = schedule.getTasks();
        task.setTime(time.toString());
        tasks.add(task);
        schedule.setTasks(tasks);
    }

    private boolean isTimeAvailable(LocalTime startTime, int duration, List<Task> tasks) {
        LocalTime endTime = startTime.plusMinutes(duration);
        for (Task task : tasks) {
            if (task.getTime() != null) {
                LocalTime taskStartTime = LocalTime.parse(task.getTime());
                LocalTime taskEndTime = taskStartTime.plusMinutes(task.getDuration());
                if (endTime.isAfter(taskStartTime) && startTime.isBefore(taskEndTime)) {
                    return false; // Time slot is not available
                }
            }
        }
        return true; // Time slot is available
    }

}
