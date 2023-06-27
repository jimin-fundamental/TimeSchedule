package TimeScheduler.project.repository;

import TimeScheduler.project.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository {
    Schedule save(Schedule schedule);


}
//    List<Task> findAllSchedule();

//    Member save(Member member);
//    Optional<Member> findById(Long id);
//    Optional<Member> findByEmail(String email);
//    List<Member> findAll();

