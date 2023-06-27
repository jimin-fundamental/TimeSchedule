//package TimeScheduler.project.service;
//
//import TimeScheduler.project.domain.Member;
//import TimeScheduler.project.repository.MemoryMemberRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*; //기본으로 들어가 있음
////junit 명령어만 기본으로 사용 가능
//
//class MemberServiceTest {
//
//    MemberService memberService;
//    MemoryMemberRepository memberRepository;
//    //여기서 new를 통해 repository 객체 생성하면, MemberService 클래스에서 이미 저장소 만들었는데 그거와 별개의 저장소를 만들게 됨
//
//    @BeforeEach
//    //BeforeEach 사용해서 동작하기 전에 MemberService에 저장소 만들어서 넣어주는 형태
//    public void beforeEach(){
//        memberRepository = new MemoryMemberRepository();
//        memberService = new MemberService(memberRepository);
//    }
//
//
//    @AfterEach
//    public void afterEach(){
//        memberRepository.clearStore();;
//    }
//
//
//    @Test
//    public void 회원가입() {//원래 join인데 어차피 테스트코드이므로 의미통하게 한국어로 바꾸는 것도 좋아
//        //Test는 Given(상황)-> When(실행, 검증하고 싶은 것) -> Then(결과)으로 실행
//
//        //Given
//        Member member = new Member();
//        member.setEmail("jimin@ewhian.net"); //멤버 만들고 이름 저장
//
//        //When
//        Long saveId = memberService.join(member); //join 서비스 검증
//        //Long은 int보다 많은 byte의 정수를 담는 자료형
//
//        //then
//        //저장소에 저장해야하므로 저장소 객체도 생성해줘야 해
//        Member findMember = memberService.findOne(saveId).get();
//        assertThat(member.getEmail()).isEqualTo(findMember.getEmail());//repository의 이름과 member로 받은 이름이 같은지 검증
//
//    }
//
//    @Test
//    public void 중복_회원_예외() throws Exception{
//        //Test에서는 예외상황도 제대로 작동되는지 체크해주는 게 중요
//        //Given
//        Member member1 = new Member();
//        member1.setEmail("jimin@ewhain.net");
//
//        Member member2 = new Member();
//        member2.setEmail("jimin@ewhain.net"); //서로 다른 객체가 동일한 이름 가졌을 때 상황 가정
//
//        //When
//        memberService.join(member1);
//        IllegalStateException e = assertThrows(IllegalStateException.class, ()-> memberService.join(member2) );
//        //member2 join 시 illegalstateException 발생한다는 의미의 람다식
//        assertEquals(e.getMessage(), "이미 존재하는 회원입니다.");
//        //Then
//    }
//
////    @Test
////    public void 로그인() {
////        // Given
////        Member member = new Member();
////        member.setEmail("jimin@ewhian.net");
////        member.setPw("password"); // 비밀번호 설정
////
////        // 회원 가입
////        memberService.join(member);
////
////        // When
////        Optional<Member> loggedInMember = memberService.login("jimin@ewhian.net", "password");
////
////        // Then
////        assertThat(loggedInMember.isPresent()).isTrue(); // 로그인된 멤버가 존재하는지 검증
////        assertThat(loggedInMember.get().getEmail()).isEqualTo(member.getEmail()); // 로그인된 멤버의 이메일이 예상과 일치하는지 검증
////    }
//
//
//
//
//}