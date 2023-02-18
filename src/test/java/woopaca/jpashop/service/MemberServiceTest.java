package woopaca.jpashop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import woopaca.jpashop.domain.Member;
import woopaca.jpashop.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입")
    void join() throws Exception {
        //given
        Member member = new Member();
        member.setName("test");

        //when
        Long joinMemberId = memberService.join(member);

        //then
        assertThat(member).isEqualTo(memberRepository.findOne(joinMemberId));
    }

    @Test
    @DisplayName("중복 회원 예외")
    void duplicateMember() throws Exception {
        //given
        Member memberA = new Member();
        Member memberB = new Member();
        memberA.setName("woopaca");
        memberB.setName("woopaca");

        //when
        memberService.join(memberA);

        //then
        assertThrows(IllegalStateException.class, () -> memberService.join(memberB));
    }
}