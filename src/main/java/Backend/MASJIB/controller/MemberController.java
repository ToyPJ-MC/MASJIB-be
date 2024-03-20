package Backend.MASJIB.controller;

import Backend.MASJIB.member.dto.CreateMemberDto;
import Backend.MASJIB.member.dto.ResponseMemberByCreateDto;
import Backend.MASJIB.member.dto.ResponseMemberbyFindwithReviewDto;
import Backend.MASJIB.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "아이디로 멤버 id를 사용한 전체 조회",description = "멤버 아이디를 통해 해당 멤버의 리뷰 정보까지 조회")
    public ResponseEntity getMemberById(@PathVariable Long id){
        try{
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if(username == null){
                return ResponseEntity.badRequest().body("로그인이 필요합니다.");
            }
            else{
                ResponseMemberbyFindwithReviewDto member=memberService.findMemberById(id);
                return ResponseEntity.ok().body(member);
            }
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
