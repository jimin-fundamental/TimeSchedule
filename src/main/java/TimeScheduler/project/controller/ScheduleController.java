package TimeScheduler.project.controller;

import TimeScheduler.project.domain.Schedule;
import TimeScheduler.project.domain.Task;
import TimeScheduler.project.repository.ScheduleRepository;
import TimeScheduler.project.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class ScheduleController {
    private final CalendarService calendarService;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleController(CalendarService calendarService, ScheduleRepository scheduleRepository) {
        this.calendarService = calendarService;
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping(value = "/Calendar")
    public String showCalendar(){
        return "Calendar";
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
    public String showFixedSchedule() {
        return "FixedSchedule";
    }

    @GetMapping(value = "/DailyWork")
    public String showDailyWork() {
        return "DailyWork";
    }


    @PostMapping(value = "/daily", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> makeSchedule(@RequestBody Schedule schedule) throws IOException {
        // Process the schedule & save
        calendarService.createSchedule(schedule.getTasks());
        return ResponseEntity.ok().build(); // return a 200 OK status
    }


    @GetMapping(value = "/getTasks")
    @ResponseBody
    public List<Task> getTasks() {
        // Retrieve all tasks from all schedules
        return scheduleRepository.findAllTasks();
    }





}
