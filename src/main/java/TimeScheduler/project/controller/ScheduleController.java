package TimeScheduler.project.controller;

import TimeScheduler.project.domain.Member;
import TimeScheduler.project.domain.Schedule;
import TimeScheduler.project.service.CalendarService;
import TimeScheduler.project.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class ScheduleController {
//    private final CalendarService calendarService;
//
//    @Autowired
//    public ScheduleController(CalendarService calendarService) {
//        this.calendarService = calendarService;
//    }

//    @GetMapping(value = "/Calendar")
//    public String Select(){
//        return "Calendar";
//    }
    @PostMapping(value = "/Calendar")
    public String SelectDate(Task task){//제대로 작동이 안돼
        //html에서 post 방식으로 날짜 받기
        Schedule schedule = new Schedule(); //날짜를 html에서 받아와 타임리프 방식으로
//        member.setEmail(f.getEmail());
//        member.setPw(form.getPw());
//        memberService.join(member);
        return "FixedSchedule";
    }



    @GetMapping(value = "/fixed")
    public String Select(){
        return "FixedSchedule";
    }

    @GetMapping(value = "/daily")
    public String dailyWork(){
        return "DailyWork";
    }

}
