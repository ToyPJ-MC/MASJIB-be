package Backend.MASJIB.member.service;

import Backend.MASJIB.jwt.provider.TokenProvider;
import Backend.MASJIB.member.dto.CreateMemberDto;
import Backend.MASJIB.member.dto.ResponseMemberByCreateDto;
import Backend.MASJIB.member.dto.ResponseMemberbyFindwithReviewDto;
import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.member.entity.Role;
import Backend.MASJIB.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider jwtProvider;

    public MemberService(MemberRepository memberRepository, TokenProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
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

    @Transactional
    public boolean modifyMemberNickName(String memberEmail, String hopeNickName){
        if(isNickNameUique(hopeNickName)) return false;
        else{
            Optional<Member> findMember = memberRepository.findByEmail(memberEmail);
            findMember.orElseThrow(RuntimeException::new);

            findMember.get().setNickname(hopeNickName);
            //memberRepository.save(findMember.get());

            return true;
        }
    }
    private boolean isNickNameUique(String nickName){
        return memberRepository.existsByNickname(nickName);
    }

}
