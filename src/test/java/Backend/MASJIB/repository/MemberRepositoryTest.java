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
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp(){
        Member member = Member
                .builder()
                .name("지우")
                .nickname("포켓몬 마스터")
                .email("test@test.com")
                .createTime(LocalDateTime.now().withNano(0))
                .shops(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();
        memberRepository.save(member);
    }
    @Test
    @DisplayName("등록된 멤버 조회 테스트")
    void 멤버조회테스트(){
        String findName = "지우";
        Optional<Member> findMember = memberRepository.findByName(findName);
        findMember.orElseThrow(RuntimeException::new);
        assertThat(findMember.get().getName()).isEqualTo(findName);
    }

    @Test
    @DisplayName("멤버 update 테스트")
    void 멤버_정보_업데이트(){
        String findName = "지우";
        String updateName = "웅이";
        Optional<Member> findMember = memberRepository.findByName(findName);
        findMember.orElseThrow(RuntimeException::new);
        findMember.get().setName(updateName);
        memberRepository.save(findMember.get());
        assertThat(findMember.get().getName()).isEqualTo(updateName);
    }

    @Test
    @DisplayName("멤버 삭제 테스트")
    void 멤버_삭제_테스트(){
        String findName = "지우";
        Optional<Member> findMember = memberRepository.findByName(findName);
        findMember.orElseThrow(RuntimeException::new);

        memberRepository.delete(findMember.get());
    }
}
