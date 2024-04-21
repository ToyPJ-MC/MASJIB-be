package Backend.MASJIB.controller;

import Backend.MASJIB.jwt.dto.ResponseTokenDto;
import Backend.MASJIB.jwt.provider.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oauth")
public class OauthController {
    private final TokenProvider tokenProvider;

    public OauthController( TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
    @Operation(summary = "카카오 로그인",description = "카카오 로그인 시 refresh token으로 새로운 토큰 발급한다.")
    @GetMapping("/refresh")
    public ResponseEntity Login(@RequestParam String refreshToken){
        ResponseTokenDto dto = tokenProvider.reGenerateToken(refreshToken);
        return ResponseEntity.ok().header("Set-Cookie",CookieSetAccessTokenValue(dto)).body(dto);
    }
    private String CookieSetAccessTokenValue(ResponseTokenDto dto){
        return "access-token="+dto.getAccessToken()+"; Path=/; Max-Age="+dto.getAccessTokenExpiresIn()+"; HttpOnly";
    }

    @Operation(summary = "로그아웃 기능", description = "로그인 되어있는 계정을 로그아웃 시킨다.")
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()") //인증된 사용자만 해당 메소드 호출 가능
    public ResponseEntity logout(){
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        tokenProvider.removeToken(memberEmail);
        return new ResponseEntity("로그아웃 되었습니다.", HttpStatus.OK);
    }
}
