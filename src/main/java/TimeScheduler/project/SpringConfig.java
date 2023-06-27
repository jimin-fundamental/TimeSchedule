package TimeScheduler.project;

import TimeScheduler.project.repository.ScheduleRepository;
import TimeScheduler.project.repository.JpaScheduleRepository;
import TimeScheduler.project.service.CalendarService;
import TimeScheduler.project.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration
@EnableTransactionManagement
public class SpringConfig {

    @PersistenceContext
    private EntityManager em;

    @Bean
    public ScheduleRepository scheduleRepository() {
        return new JpaScheduleRepository(em);
    }

    @Bean
    public CalendarService calendarService(OpenAiService openAiService) {
        return new CalendarService(scheduleRepository(), openAiService);
    }

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService();
    }
}
