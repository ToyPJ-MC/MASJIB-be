package Backend.MASJIB.controller;

import Backend.MASJIB.jwt.dto.ResponseTokenDto;
import Backend.MASJIB.jwt.provider.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity login(@RequestParam String refreshToken){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username ==null) return ResponseEntity.status(401).body("인증되지 않은 사용자입니다.");
        else {
            ResponseTokenDto dto = tokenProvider.reGenerateToken(refreshToken);
            return ResponseEntity.ok().header("Set-Cookie",CookieSetAccessTokenValue(dto)).body(dto);
        }
    }
    private String CookieSetAccessTokenValue(ResponseTokenDto dto){
        return "accessToken="+dto.getAccessToken()+"; Max-Age="+dto.getAccessTokenExpiresIn()+"; Path=/; HttpOnly";
    }
}
