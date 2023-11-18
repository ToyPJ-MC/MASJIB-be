package Backend.MASJIB.controller;

import Backend.MASJIB.member.dto.CreateMemberDto;
import Backend.MASJIB.member.dto.ResponseMemberByCreateDto;
import Backend.MASJIB.member.dto.ResponseMemberbyFindDto;
import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    /*@GetMapping("/test")
    @Operation(summary = "테스트")
    public ResponseEntity createMember(){
        try{
            Member member = memberService.findMemberById();
            return ResponseEntity.ok().body(member);
        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }*/

    @PostMapping("/post")
    @Operation(summary = "멤버 등록 api")
    public ResponseEntity createMember(@RequestBody CreateMemberDto dto){
        try{
           ResponseMemberByCreateDto responseDto =memberService.createMember(dto);
            return ResponseEntity.ok().body(responseDto);
        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    @Operation(summary = "아이디로 멤버 id를 사용한 전체 조회",description = "멤버 아이디를 통해 해당 멤버의 리뷰정보까지 조회")
    public ResponseEntity getMemberById(@PathVariable Long id){
        try{
            ResponseMemberbyFindDto member=memberService.findMemberById(id);
            return ResponseEntity.ok().body(member);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
