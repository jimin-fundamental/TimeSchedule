//package TimeScheduler.project.controller;
//
//import TimeScheduler.project.domain.Member;
//import TimeScheduler.project.service.MemberService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.Calendar;
//import java.util.Optional;
//
//@Controller
//public class MemberController {
//    private final MemberService memberService;
//
//    @Autowired
//    public MemberController(MemberService memberService) {
//        this.memberService = memberService;
//    }
//
//    @GetMapping(value = "/login")
//    public String createForm(Model model) {
//        model.addAttribute("loginForm", new LoginForm()); //이거 없으면 login 페이지 안 떠
//        return "login";
//    }
//
//    @PostMapping(value = "/login")
//    public String login(MemberForm form) {
//        Member member = new Member();
//        member.setEmail(form.getEmail());
//        member.setPw(form.getPw());
//
//        Optional<Member> loggedInMember = memberService.login(member);
//        if (loggedInMember.isPresent()) {
//            // 로그인 성공
//            // 적절한 처리를 수행하고, 로그인된 회원 정보를 활용합니다.
//            return "Calendar";
//        } else {
//            // 로그인 실패
//            // 적절한 실패 처리를 수행합니다.
//            return "redirect:/login?error";
//        }
//    }
//
//    @GetMapping(value="/signup")
//    public String signup() {
//        return "signup";
//    }
//    @PostMapping(value = "/signup")
//    public String create(MemberForm form){
//        Member member = new Member();
//        member.setEmail(form.getEmail());
//        member.setPw(form.getPw());
//        memberService.join(member);
//        return "redirect:/login";
//    }



//
//}
