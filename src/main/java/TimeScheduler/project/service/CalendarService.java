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
        List<Task> scheduledTasks = null;

        if (!flexibleTasks.isEmpty()) {
            flexibleTasks.sort(Comparator.comparingInt(Task::getPriority)); // Sort flexible tasks by priority

            System.out.println("이제 flexible task 배정 시작할게요!");
            System.out.println();
//            for (Task task : flexibleTasks) {
//                assignTask(schedule, task, fixedTasks, flexibleTasks);
//                //여기서 openai를 통한 flexibleTask의 배정이 끝
//                //모든 내용은 schedule에 저장
//            }
            assignTask(schedule, fixedTasks, flexibleTasks);


//            scheduledTasks = schedule.getTasks();

        }


        // Print schedule tasks to console
        System.out.println();
        System.out.println("schedule 출력중");
        for (Task task : schedule.getTasks()) {
            System.out.println("Name: " + task.getName() +
                    ", Fixed: " + task.isFixed() +
                    ", StartTime: " + task.getStartTime() +
                    ", EndTime: " + task.getEndTime());
        }


        // Save the schedule
        memberRepository.save(schedule);

        return schedule;
    }

    private void assignFixedTask(Schedule schedule, Task task) {
        List<Task> tasks = schedule.getTasks();
        tasks.add(task);
        schedule.setTasks(tasks);
    }

    //new 버전
    private void assignTask(Schedule schedule, List<Task> fixedTasks, List<Task> flexibleTasks) throws IOException {
        // Use OpenAI to fetch updated daily schedule
        List<String> assignedTasks = openAi.fetchUpdatedSchedule(fixedTasks, flexibleTasks);
        System.out.println("CalendarService's assignTask method");

        // Define the regular expression pattern
        Pattern pattern = Pattern.compile("TaskName: (.*), StartTime: (.*), EndTime: (.*)");

        List<Task> tasks = schedule.getTasks();  // Get existing tasks from schedule

        // Iterate over the assigned tasks
        for (String taskString : assignedTasks) {
            // Apply the regular expression to extract the task details
            Matcher matcher = pattern.matcher(taskString);
            if (matcher.find()) {
                String taskName = matcher.group(1);
                String startTime = matcher.group(2);
                String endTime = matcher.group(3);

                // Create a new Task object for each assigned task
                Task assignedTask = new Task();

                // Use the extracted task details as needed
                assignedTask.setName(taskName);
                assignedTask.setStartTime(LocalTime.parse(startTime));
                assignedTask.setEndTime(LocalTime.parse(endTime));

                tasks.add(assignedTask);
            }
        }

        schedule.setTasks(tasks);
    }


    //old 버전
//    private void assignTask(Schedule schedule, List<Task> fixedTasks, List<Task> flexibleTasks) throws IOException {
//
//        // Use OpenAI to fetch updated daily schedule
//        List<String> assignedTasks = openAi.fetchUpdatedSchedule(fixedTasks, flexibleTasks);
//        int numofTask = assignedTasks.size();
//        System.out.println("CalenderService의 assignTask함수입니다.");
//        //여기까지 작동됨.
//
//        //assignedTasks array가 return
//        //TaskName: 놀기, StartTime: 09:30, EndTime: 11:00
//        //TaskName: 냐옹, StartTime: 13:00, EndTime: 16:20
//
//        // Add the task to the schedule
//        // Define the regular expression pattern
//        Pattern pattern = Pattern.compile("TaskName: (.*), StartTime: (.*), EndTime: (.*)");
//
//        List<Task> tasks = schedule.getTasks();
//
//
//        // Iterate over the assigned tasks
//        for (String taskString : assignedTasks) {
//            // Apply the regular expression to extract the task details
//            Matcher matcher = pattern.matcher(taskString);
//            if (numofTask > 0 && matcher.find()) {
//                String taskName = matcher.group(1);
//                String startTime = matcher.group(2);
//                String endTime = matcher.group(3);
//
//                // Use the extracted task details as needed
//                System.out.println("TaskName: " + taskName);
//                task.setName(taskName);
//                //Parse the start and end time strings to LocalTime
//                LocalTime assignedStartTime = LocalTime.parse(startTime);
//                LocalTime assignedEndTime = LocalTime.parse(endTime);
//                System.out.println("StartTime: " + startTime);
//                task.setStartTime(assignedStartTime);
//                System.out.println("EndTime: " + endTime);
//                task.setEndTime(assignedEndTime);
//                tasks.add(task);
//                numofTask--;
//            }
//            schedule.setTasks(tasks);
//        }
//
//        //이전 버전
////    private void assignTask(Schedule schedule, Task task, List<Task> fixedTasks, List<Task> flexibleTasks) throws IOException {
////        // Use OpenAI to fetch updated daily schedule
////        List<String> assignedTasks = openAi.fetchUpdatedSchedule(fixedTasks, flexibleTasks);
////        System.out.println("CalenderService의 assignTask함수입니다.");
////        //여기까지 작동됨.
////
////        //assignedTasks array가 return
////
////        // Check if the task is among the assigned tasks
////        String taskName = "TaskName: " + task.getName();
////        for (String assignedTask : assignedTasks) {
////            if (assignedTask.startsWith(taskName)) {
////                // Extract the start and end time from the assigned task
////                String startTimeStr = extractStartTime(assignedTask);
////                String endTimeStr = extractEndTime(assignedTask);
////
////                // Parse the start and end time strings to LocalTime
////                LocalTime assignedStartTime = LocalTime.parse(startTimeStr);
////                LocalTime assignedEndTime = LocalTime.parse(endTimeStr);
////
////                // Update the task with the assigned start and end time
////                task.setStartTime(assignedStartTime);
////                task.setEndTime(assignedEndTime);
////
////                // Add the task to the schedule
////                List<Task> tasks = schedule.getTasks();
////                tasks.add(task);
////                schedule.setTasks(tasks);
////            }
////        }
////
////        // If the task was not assigned, assign it with the original start time
//////        task.setStartTime(startTime);
//////        task.setEndTime(startTime.plusMinutes(task.getDuration()));
////
////        // Add the task to the schedule
////        List<Task> tasks = schedule.getTasks();
////        tasks.add(task);
////        schedule.setTasks(tasks);
////    }
//
//
//    }
}
