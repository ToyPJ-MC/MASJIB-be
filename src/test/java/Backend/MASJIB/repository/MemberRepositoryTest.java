package Backend.MASJIB.repository;

import Backend.MASJIB.member.entity.Member;

import Backend.MASJIB.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp(){
        // 멤버 생성
        Member member = Member
                .builder()
                .nickname("@user-a1da23")
                .email("test@test.com")
                .createTime(LocalDateTime.now().withNano(0))
                .shops(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();
        memberRepository.save(member);
    }
    @Test
    @DisplayName("등록된 멤버를 e-mail로 조회 테스트")
    void 멤버조회테스트(){
        String findEmail = "test@test.com";

        Optional<Member> findMember = memberRepository.findByEmail(findEmail);
        findMember.orElseThrow(RuntimeException::new);

        assertThat(findMember.get().getEmail()).isEqualTo(findEmail);
    }

    @Test
    @DisplayName("멤버의 닉네임 변경 테스트")
    void 멤버_정보_업데이트(){
        String findEmail = "test@test.com";
        String changeNickName = "엄마커서 랄로가될래요";
        Optional<Member> findMember = memberRepository.findByEmail(findEmail);
        findMember.orElseThrow(RuntimeException::new);

        findMember.get().setNickname(changeNickName);
        memberRepository.save(findMember.get());
        assertThat(findMember.get().getNickname()).isEqualTo(changeNickName);
    }

    @Test
    @DisplayName("멤버 삭제 테스트")
    void 멤버_삭제_테스트(){
        String findEmail = "test@test.com";
        Optional<Member> findMember = memberRepository.findByEmail(findEmail);
        findMember.orElseThrow(RuntimeException::new);

        memberRepository.delete(findMember.get());
    }

}
