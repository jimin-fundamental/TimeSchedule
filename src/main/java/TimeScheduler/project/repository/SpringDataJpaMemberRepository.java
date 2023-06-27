package TimeScheduler.project.repository;

//import TimeScheduler.project.domain.Member;
import TimeScheduler.project.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Schedule, Long>, MemberRepository {
//    @Override
//    Optional<Member> findByEmail(String email);
}
