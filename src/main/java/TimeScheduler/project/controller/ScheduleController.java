package TimeScheduler.project.controller;

import TimeScheduler.project.domain.Schedule;
import TimeScheduler.project.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    public String showCalendar(){
        return "Calendar";
    }

//    @PostMapping(value = "/Calendar")
//    public String selectDate(@ModelAttribute("task") Task task) {
//        Schedule schedule = new Schedule();
//        schedule.setDate(task.getDate());
//
//        // Process the schedule & save
//        schedule = calendarService.createSchedule(schedule);
//
//        return "FixedSchedule";
//    }

//    @PostMapping(value = "/signup")
//    public String create(MemberForm form){
//        Member member = new Member();
//        member.setEmail(form.getEmail());
//        member.setPw(form.getPw());
//        memberService.join(member);
//        return "redirect:/login";
//    }


    @GetMapping(value = "/fixed")
    public String showFixedSchedule() {
        return "FixedSchedule";
    }

    @GetMapping(value = "/daily")
    public String showDailyWork() {
        return "DailyWork";
    }

//    @PostMapping("/daily")
//    public String makeSchedule(@ModelAttribute("task") Task task) throws IOException {
//        List<Task> tasks = new ArrayList<>();
//        tasks.add(task);
//
//        Schedule schedule = new Schedule();
//
//        // Process the schedule & save
//        schedule = calendarService.createSchedule(tasks);
//
//        // Redirect to the "DailyWork" page after processing the schedules
//        return "redirect:/DailyWork";
//    }

    @PostMapping("/daily")
    public String makeSchedule(@RequestBody Schedule schedule) throws IOException {
        // Process the schedule & save
        schedule = calendarService.createSchedule(schedule.getTasks());

        // Redirect to the "DailyWork" page after processing the schedules
        return "redirect:/DailyWork";
    }



}
