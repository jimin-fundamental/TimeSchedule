package TimeScheduler.project.repository;

import TimeScheduler.project.domain.Schedule;
import jakarta.persistence.EntityManager;

public class JpaMemberRepository implements MemberRepository{

    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Schedule save(Schedule schedule) {
        em.persist(schedule);
        return schedule;
    }
}
//    @Override
//    public List<Task> findAllSchedule() {
//        return em.createQuery("select t from Task t", )
//                .getResultList();
//    }

//    @Override
//    public Member save(Member member) {
//        em.persist(member);
//        return member;
//    }
//
//    @Override
//    public Optional<Member> findById(Long id) {
//        Member member = em.find(Member.class, id);
//        return Optional.ofNullable(member);
//    }
//
//    @Override
//    public Optional<Member> findByEmail(String email) {
//        List<Member> result = em.createQuery("select m from Member m where m.email = :email", Member.class) //이정도는 직접 작성해줘야 됨 "JPQL"
//                .setParameter("email", email)
//                .getResultList();
//        return result.stream().findAny();
//    }
//
//    @Override
//    public List<Member> findAll() {
//        return em.createQuery("select m from Member m", Member.class)
//                .getResultList();
//    }

