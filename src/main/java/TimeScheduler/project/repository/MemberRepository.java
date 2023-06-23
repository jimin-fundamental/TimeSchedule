package TimeScheduler.project.repository;

import TimeScheduler.project.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {//db 아직 결정되지 않아서 interface 사용
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    List<Member> findAll();
}
