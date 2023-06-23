package TimeScheduler.project.service;

import TimeScheduler.project.domain.Member;
import TimeScheduler.project.repository.MemberRepository;
import TimeScheduler.project.repository.MemoryMemberRepository;

import java.util.Optional;

public class MemberService {
    private final MemberRepository memberRepository = new MemoryMemberRepository();

    /**
     * 회원가입
     */
    public Long join(Member member){
        //같은 이메일이 있는 중복회원 X
        Optional<Member> result = memberRepository.findByEmail(member.getEmail());
//        result.ifPresent(m -> {
//            throw new IllegaleStateException("이미 존재하는 회원입니다.");
//        });

        memberRepository.save(member);
        return member.getId();
    }
}
