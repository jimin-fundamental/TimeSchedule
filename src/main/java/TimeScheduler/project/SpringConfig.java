package TimeScheduler.project;

import TimeScheduler.project.repository.MemberRepository;
import TimeScheduler.project.service.CalendarService;
//import TimeScheduler.project.service.MemberService;
import TimeScheduler.project.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

//    @Bean
//    public MemberService memberService() {
//        return new MemberService(memberRepository);
//    }

    @Bean
    public CalendarService calendarService(OpenAiService openAiService) {
        return new CalendarService(memberRepository, openAiService);
    }

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService();
    }
}

