package TimeScheduler.project.controller;

import TimeScheduler.project.domain.Member;
import TimeScheduler.project.domain.Schedule;
import TimeScheduler.project.service.CalendarService;
import TimeScheduler.project.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ScheduleController {
    private final CalendarService calendarService;

    @Autowired
    public ScheduleController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping(value = "/Calendar")
    public String Calendar(){
        return "Calendar";
    }
    @PostMapping(value = "/Calendar")
    public String SelectDate(Task task){//제대로 작동이 안돼
        //html에서 post 방식으로 날짜 받기
        Schedule schedule = new Schedule(); //날짜를 html에서 받아와 타임리프 방식으로

        return "FixedSchedule";
    }

//    @PostMapping(value = "/signup")
//    public String create(MemberForm form){
//        Member member = new Member();
//        member.setEmail(form.getEmail());
//        member.setPw(form.getPw());
//        memberService.join(member);
//        return "redirect:/login";
//    }


    @GetMapping(value = "/fixed")
    public String Select(){
        return "FixedSchedule";
    }

    @GetMapping(value = "/daily")
    public String dailyWork(){
        return "DailyWork";
    }

    @PostMapping("/daily")
    public String MakeSchedule(@ModelAttribute("task") Task task) throws IOException {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        List<Schedule> schedules = (List<Schedule>) calendarService.createSchedule(tasks);

        for (Schedule schedule : schedules) {
            // Set any necessary properties of the schedule
            schedule.setDate(task.getDate()); // Set the date based on the received task

            // You can set other properties of the schedule if needed
            // schedule.setXXX(task.getXXX());

            // Perform any additional operations on the schedule as required
            // ...

            // Save or process the schedule
            // ...

            // If you want to save the schedule to a database, you can use a service method
            // Example: scheduleService.saveSchedule(schedule);

            // If you want to process the schedule further, you can perform additional logic here
            // ...

            // Add the schedule to the list of schedules
            // schedules.add(schedule);
        }

        // Redirect to the "DailyWork" page after processing the schedules
        return "redirect:/DailyWork";
    }



}
