package TimeScheduler.project.repository;

import TimeScheduler.project.domain.Schedule;
import TimeScheduler.project.domain.Task;
import jakarta.persistence.EntityManager;

import java.util.List;

public class JpaScheduleRepository implements ScheduleRepository {

    private final EntityManager em;

    public JpaScheduleRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Schedule save(Schedule schedule) {
        em.persist(schedule);
        return schedule;
    }

    @Override
    public List<Task> findAllTasks() {
        return em.createQuery("select t from Task t", Task.class)
                .getResultList();
    }
}
