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

        // Use OpenAI to fetch updated daily schedule
        openAi.fetchUpdatedSchedule(fixedTasks, flexibleTasks);

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
                        assignTask(schedule, task, startTime, endTime);
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

    private void assignTask(Schedule schedule, Task task, LocalTime startTime, LocalTime endTime) {
        List<Task> tasks = schedule.getTasks();
        task.setStartTime(startTime);
        task.setEndTime(endTime);
        tasks.add(task);
        schedule.setTasks(tasks);
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
