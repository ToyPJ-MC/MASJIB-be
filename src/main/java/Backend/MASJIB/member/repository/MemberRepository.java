package Backend.MASJIB.member.repository;

import Backend.MASJIB.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository  extends JpaRepository<Member,Long> {

    Optional<Member> findByName(String name);
    Boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
    boolean existsById (Long id);
}
