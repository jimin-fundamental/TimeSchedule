package TimeScheduler.project.repository;

import TimeScheduler.project.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemoryMemberRepositoryTest {
    MemoryMemberRepository repository = new MemoryMemberRepository();
    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }//테스트 끝날 때마다 구현체 클리어-> 순서 상관없이 하기 위해

    @Test
    public void save(){
        Member member = new Member();
        member.setEmail("jimin@ewhain.net");

        repository.save(member);

        Member result = repository.findById(member.getId()).get();
//        System.out.println("result = " + (result == member));
//        Assertions.assertEquals(result, member);//두개가 같은지 확인하는 함수
        assertThat(member).isEqualTo(result);
    }

    @Test
    public void findByEmail(){
        Member member1 = new Member();
        member1.setEmail("jimin1@ewhain.net");
        repository.save(member1);

        Member member2 = new Member();
        member2.setEmail("jimin2@ewhain.net");
        repository.save(member2);

        Member result = repository.findByEmail("jimin1@ewhain.net").get();
        assertThat(result).isEqualTo(member1);

    }

    @Test
    public void findAll(){
        Member member1 = new Member();
        member1.setEmail("jimin1@ewhain.net");
        repository.save(member1);

        Member member2 = new Member();
        member2.setEmail("jimin2@ewhain.net");
        repository.save(member2);

        List<Member> result = repository.findAll();
        assertThat(result.size()).isEqualTo(2);
    }


}
