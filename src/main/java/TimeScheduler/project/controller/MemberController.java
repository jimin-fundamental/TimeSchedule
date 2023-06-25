package TimeScheduler.project.controller;

import TimeScheduler.project.domain.Member;
import TimeScheduler.project.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping(value="/login")
    public String createForm() {
        return "login";
    }

    @PostMapping(value = "/login")
    public String create(MemberForm form){
        Member member = new Member();
        member.setEmail(form.getEmail());
        memberService.join(member);
        return "Calendar";

    }

    @GetMapping(value = "/Calendar")
    public String SelectDate(){
        return "Calendar";

    }


}
