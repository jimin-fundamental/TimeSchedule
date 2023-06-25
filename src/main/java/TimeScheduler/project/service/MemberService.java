package TimeScheduler.project.service;

import TimeScheduler.project.domain.Member;
import TimeScheduler.project.repository.MemberRepository;
import TimeScheduler.project.repository.MemoryMemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {//외부에서 저장소 넣어주도록
        this.memberRepository = memberRepository;
    }

    //회원가입
    public Long join(Member member){
        //중복회원 검증 후 -> 저장
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> { //optional로 null 감쌌기 때문에 이렇게 처리 가능
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                    //throw: 개발자가 강제로 exception 발생시키는 코드 => 예외처리하기 위한 목적
                    //사용자가 직접 예외를 발생시켜 주고 싶은 부분에 throw new XXXException();을 통하여 예외를 발생
                    //IllegalStateException통해 exception 재정의해서 예외 처리를 함
                });
    }

    //전체 회원 조회
    public List<Member> findMembers(){
        //List: Array(배열)과 유사한 자료형
        // List<Integer> listA = new ArrayList<>();     // 초기 용량은 10
        // List<Integer> listB = new ArrayList<>(100);  // 용량이 100인 리스트 생성
        // List<String> cities = Arrays.asList("Amsterdam", "Paris", "London")
        return memberRepository.findAll();

    }

    //아이디로 회원 조회
    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }

    public Optional<Member> login(String email, String password) {
        return memberRepository.findByEmail(email)
                .filter(member -> member.getPw().equals(password));
    }


}

