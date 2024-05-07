package Backend.MASJIB.controller;

import Backend.MASJIB.exception.LoginRequiredException;
import Backend.MASJIB.member.dto.CreateMemberDto;
import Backend.MASJIB.member.dto.ResponseMemberByCreateDto;
import Backend.MASJIB.member.dto.ResponseMemberbyFindwithReviewDto;
import Backend.MASJIB.member.service.MemberService;
import Backend.MASJIB.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@Slf4j
@Tag(name="Member API")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "아이디로 멤버 id를 사용한 전체 조회",description = "멤버 아이디를 통해 해당 멤버의 리뷰 정보까지 조회")
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_USER')") //인증된 사용자만 해당 메소드 호출 가능
    public ResponseEntity getMemberById(@PathVariable Long id){
        try{
            String memberEmail = SecurityUtil.getCurrentMemberEmail().orElseThrow(LoginRequiredException::new);
            ResponseMemberbyFindwithReviewDto member=memberService.findMemberById(id);
            return ResponseEntity.ok().body(member);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/{nickname}")
    @Operation(summary = "닉네임 변경 api",description ="사용하는 닉네임을 변경하는 api")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity setMemberNickName(@PathVariable String nickname){
        try{
            String memberEmail = SecurityUtil.getCurrentMemberEmail().orElseThrow(LoginRequiredException::new);
             boolean isNickNameUique= memberService.modifyMemberNickName(memberEmail, nickname);
            if(isNickNameUique) return ResponseEntity.ok("\""+nickname + "\"로 변경 되었습니다.");
            return ResponseEntity.badRequest().body("중복된 닉네임입니다.");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/info")
    @Operation(summary = "닉네임 가져오기 api", description = "")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity getMemberInfo(){
        try{
            String memberEmail = SecurityUtil.getCurrentMemberEmail().orElseThrow(LoginRequiredException::new);
            String memberNickName = memberService.findNickNameByEmail(memberEmail);
            return ResponseEntity.ok(memberNickName);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
