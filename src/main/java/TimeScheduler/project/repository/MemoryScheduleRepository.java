package TimeScheduler.project.repository;

import TimeScheduler.project.domain.Schedule;
import TimeScheduler.project.domain.Task;

import java.util.*;
import java.util.stream.Collectors;

public class MemoryScheduleRepository implements ScheduleRepository{
    private static Map<Long, Schedule> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Schedule save(Schedule schedule) {
        schedule.setId(++sequence);
        store.put(schedule.getId(), schedule);
        return schedule;
    }

    @Override
    public List<Task> findAllTasks() {
        return store.values().stream()
                .flatMap(schedule -> schedule.getTasks().stream())
                .collect(Collectors.toList());
    }
}

//    @Override
//    public List<Task> findAllSchedule() {
//        return new ArrayList<>(store.values());
//    }

//    @Override
//    public Member save(Member member) {
//        member.setId(++sequence);
//        store.put(member.getId(),member);
//        return member;
//    }
//
//    @Override
//    public Optional<Member> findById(Long id) {
//        return Optional.ofNullable(store.get(id)); //Nullable해서 optional하게 감싸기
//    }
//
//    @Override
//    public Optional<Member> findByEmail(String email) {
//        return store.values().stream()
//                .filter(member -> member.getEmail().equals(email))
//                .findAny();//하나라도 찾으면 반환됨
//    }
//
//    @Override
//    public List<Member> findAll() {
//        return new ArrayList<>(store.values());
//    }
//
//    public void clearStore(){
//        store.clear();
//    }

