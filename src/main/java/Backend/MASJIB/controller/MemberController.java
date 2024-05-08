package Backend.MASJIB.controller;

import Backend.MASJIB.member.dto.ResponseMemberbyFindwithReviewDto;
import Backend.MASJIB.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@Slf4j
@Tag(name="Member API",description = "멤버 API")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "아이디로 멤버 id를 사용한 전체 조회",description = "멤버 아이디를 통해 해당 멤버의 리뷰 정보까지 조회합니다.")
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_USER')") //인증된 사용자만 해당 메소드 호출 가능
    public ResponseEntity getMemberById(@PathVariable Long id){
        try{
            String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            ResponseMemberbyFindwithReviewDto member=memberService.findMemberById(id);
            return ResponseEntity.ok().body(member);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/{nickname}")
    @Operation(summary = "닉네임 변경 api",description ="사용하는 닉네임을 변경합니다.")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity setMemberNickName(@PathVariable String nickname){
        try{
            String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
             boolean isNickNameUique= memberService.modifyMemberNickName(memberEmail, nickname);
            if(isNickNameUique) return ResponseEntity.ok("\""+nickname + "\"로 변경 되었습니다.");
            return ResponseEntity.badRequest().body("중복된 닉네임입니다.");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/info")
    @Operation(summary = "닉네임 가져오기 api", description = "멤버의 등록된 닉네임을 가져옵니다.")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity getMemberInfo(){
        try{
            String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            String memberNickName = memberService.findNickNameByEmail(memberEmail);
            return ResponseEntity.ok(memberNickName);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
