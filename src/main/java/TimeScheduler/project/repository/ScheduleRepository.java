package TimeScheduler.project.repository;

import TimeScheduler.project.domain.Schedule;
import TimeScheduler.project.domain.Task;

import java.util.List;

public interface ScheduleRepository {
    Schedule save(Schedule schedule);
    List<Task> findAllTasks();
}




//    List<Task> findAllSchedule();

//    Member save(Member member);
//    Optional<Member> findById(Long id);
//    Optional<Member> findByEmail(String email);
//    List<Member> findAll();

