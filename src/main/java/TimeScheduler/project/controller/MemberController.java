package TimeScheduler.project.controller;

import TimeScheduler.project.domain.Member;
import TimeScheduler.project.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Calendar;
import java.util.Optional;

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
    public String login(LoginForm form) {
        String email = form.getEmail();
        String password = form.getPw();

        Optional<Member> optionalMember = memberService.login(email, password);

        if (optionalMember.isPresent()) {
            // Login successful
            // Redirect to the appropriate page (e.g., "Calendar")
            return "Calendar";
        } else {
            // Login failed
            // Redirect to the login page with an error message
            return "redirect:/login?error";
        }
    }



    @GetMapping(value="/signup")
    public String signup() {
        return "signup";
    }
    @PostMapping(value = "/signup")
    public String create(MemberForm form){
        Member member = new Member();
        member.setEmail(form.getEmail());
        member.setPw(form.getPw());
        memberService.join(member);
        return "login";
    }

    @GetMapping(value = "/Calendar")
    public String SelectDate(){
        return "Calendar";
    }


}
