package TimeScheduler.project.service;

import TimeScheduler.project.domain.Task;
import TimeScheduler.project.domain.Schedule;
import TimeScheduler.project.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional
public class CalendarService {

    private final MemberRepository memberRepository;

    private final OpenAiService openAi;

    @Autowired
    public CalendarService(MemberRepository memberRepository, OpenAiService openAi) {
        this.memberRepository = memberRepository;
        this.openAi = openAi;
    }

    @Transactional
    public Schedule createSchedule(List<Task> tasks) throws IOException {
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
        fixedTasks.sort(Comparator.comparing(Task::getStartTime));

        // Generate schedule for fixed tasks
        for (Task task : fixedTasks) {
            assignFixedTask(schedule, task);
        }

        // Generate schedule for flexible tasks
        if (!flexibleTasks.isEmpty()) {
            LocalTime startTime = LocalTime.of(7, 0); // Start time for the schedule
            LocalTime endTime = LocalTime.of(22, 0); // End time for the schedule

            flexibleTasks.sort(Comparator.comparingInt(Task::getPriority)); // Sort flexible tasks by priority

            List<Task> scheduledTasks = schedule.getTasks();
            for (Task task : flexibleTasks) {
                boolean assigned = false;
                for (LocalTime time = startTime; time.isBefore(endTime) && !assigned; time = time.plusMinutes(task.getDuration())) {
                    if (isTimeAvailable(time, task.getDuration(), scheduledTasks)) {
                        assignTask(schedule, task, time, fixedTasks, flexibleTasks);
                        assigned = true;
                    }
                }
            }
        }

        // Save the schedule
        memberRepository.save(schedule);

        // Print schedule to console
        System.out.println(schedule);

        // Print schedule tasks to console
        for (Task task : schedule.getTasks()) {
            System.out.println("Name: " + task.getName() +
                    ", Duration: " + task.getDuration() +
                    ", Fixed: " + task.isFixed() +
                    ", StartTime: " + task.getStartTime() +
                    ", EndTime: " + task.getEndTime());
        }

        return schedule;
    }

    private void assignFixedTask(Schedule schedule, Task task) {
        List<Task> tasks = schedule.getTasks();
        tasks.add(task);
        schedule.setTasks(tasks);
    }

    private void assignTask(Schedule schedule, Task task, LocalTime startTime, List<Task> fixedTasks, List<Task> flexibleTasks) throws IOException {
        // Use OpenAI to fetch updated daily schedule
        List<String> assignedTasks = openAi.fetchUpdatedSchedule(fixedTasks, flexibleTasks);

        // Check if the task is among the assigned tasks
        String taskName = "TaskName: " + task.getName();
        for (String assignedTask : assignedTasks) {
            if (assignedTask.startsWith(taskName)) {
                // Extract the start and end time from the assigned task
                String startTimeStr = extractStartTime(assignedTask);
                String endTimeStr = extractEndTime(assignedTask);

                // Parse the start and end time strings to LocalTime
                LocalTime assignedStartTime = LocalTime.parse(startTimeStr);
                LocalTime assignedEndTime = LocalTime.parse(endTimeStr);

                // Update the task with the assigned start and end time
                task.setStartTime(assignedStartTime);
                task.setEndTime(assignedEndTime);

                // Add the task to the schedule
                List<Task> tasks = schedule.getTasks();
                tasks.add(task);
                schedule.setTasks(tasks);

                return;
            }
        }

        // If the task was not assigned, assign it with the original start time
        task.setStartTime(startTime);
        task.setEndTime(startTime.plusMinutes(task.getDuration()));

        // Add the task to the schedule
        List<Task> tasks = schedule.getTasks();
        tasks.add(task);
        schedule.setTasks(tasks);
    }

    private String extractStartTime(String assignedTask) {
        // Use a regular expression to extract the start time from the assigned task string
        Pattern pattern = Pattern.compile("StartTime: (.*?\\d+:\\d+\\s*[ap]m)");
        Matcher matcher = pattern.matcher(assignedTask);

        if (matcher.find()) {
            return matcher.group(1).trim();
        } else {
            // Return a default value or handle the case when start time extraction fails
            return null;
        }
    }

    private String extractEndTime(String assignedTask) {
        // Use a regular expression to extract the end time from the assigned task string
        Pattern pattern = Pattern.compile("EndTime: (.*?\\d+:\\d+\\s*[ap]m)");
        Matcher matcher = pattern.matcher(assignedTask);

        if (matcher.find()) {
            return matcher.group(1).trim();
        } else {
            // Return a default value or handle the case when end time extraction fails
            return null;
        }
    }

    private boolean isTimeAvailable(LocalTime startTime, int duration, List<Task> tasks) {
        LocalTime endTime = startTime.plusMinutes(duration);
        for (Task task : tasks) {
            if (task.getStartTime() != null) {
                LocalTime taskStartTime = task.getStartTime();
                LocalTime taskEndTime = taskStartTime.plusMinutes(task.getDuration());
                if (endTime.isAfter(taskStartTime) && startTime.isBefore(taskEndTime)) {
                    return false; // Time slot is not available
                }
            }
        }
        return true; // Time slot is available
    }
}
