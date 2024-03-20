package Backend.MASJIB.jwt.handler;

import Backend.MASJIB.jwt.provider.TokenProvider;
import Backend.MASJIB.member.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Component
public class Oauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProvider jwtProvider;
    private final MemberRepository memberRepository;
    @Autowired
    public Oauth2AuthenticationSuccessHandler(TokenProvider jwtProvider, MemberRepository memberRepository) {
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try{
            OAuth2User member = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes =member.getAttributes();
            Map<String,Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

            String email = kakaoAccount.get("email").toString();

            SecurityContextHolder.getContext().setAuthentication(authentication); // 인증정보를 SecurityContext에 저장 -> 현재 실행 중인 스레드에 대한 보안 정보를 제공
            String refreshToken = jwtProvider.createRefreshToken(email);

            response.addHeader("Authorization","Bearer "+refreshToken); // 헤더에 토큰을 넣어줌
            String url = makeRedirectUrl(refreshToken);
            getRedirectStrategy().sendRedirect(request,response,url); // 리다이렉트
        }catch (RuntimeException e){
            throw e;
        }
    }
    private String makeRedirectUrl(String token){
        return UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/redirect")
                .queryParam("code",token)
                .build().toUriString();
    }
}
