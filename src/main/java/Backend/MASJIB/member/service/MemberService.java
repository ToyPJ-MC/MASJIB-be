package Backend.MASJIB.member.service;

import Backend.MASJIB.member.dto.CreateMemberDto;
import Backend.MASJIB.member.dto.ResponseMemberByCreateDto;
import Backend.MASJIB.member.dto.ResponseMemberbyFindwithReviewDto;
import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;


@Slf4j
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Transactional
    public ResponseMemberbyFindwithReviewDto findMemberById(Long id){
        if(!memberRepository.existsById(id)){
            throw new RuntimeException("존재하지 않는 회원입니다.");
        }
        Optional<Member> findMember = memberRepository.findById(id);
        return ResponseMemberbyFindwithReviewDto.set(findMember.get());
    }
    @Transactional
    public ResponseMemberByCreateDto createMember(CreateMemberDto dto){

        if(memberRepository.existsByEmail(dto.getEmail())){
           throw new RuntimeException("이미 존재하는 회원입니다.");
        }
        Member createMember = Member.builder()
                .name(dto.getName())
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .createTime(LocalDateTime.now())
                .reviews(new ArrayList<>())
                .shops(new ArrayList<>())
                .build();
        memberRepository.save(createMember);
        return ResponseMemberByCreateDto.set(createMember);
    }
}
