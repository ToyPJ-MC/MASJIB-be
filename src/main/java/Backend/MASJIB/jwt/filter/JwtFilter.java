package Backend.MASJIB.jwt.filter;

import Backend.MASJIB.jwt.provider.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;


@Component
@Slf4j
public class JwtFilter extends GenericFilterBean { // 토큰의 인증정보를 SecurityContext에 저장하는 역할
    private final TokenProvider jwtProvider;

    public JwtFilter(TokenProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException { // 토큰의 인증정보를 SecurityContext에 저장하는 역할
        HttpServletRequest httpRequest =(HttpServletRequest) request;
        String jwt = resolveToken(httpRequest);

        if(StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)){
            Authentication authentication = jwtProvider.getAuthentication(jwt); // 토큰으로 인증 정보를 조회
            SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext에 인증 정보를 저장
            log.info("Security에 '{}' 인증정보를 저장했습니다.",authentication.getName());
        }
        chain.doFilter(request,response);
    }

    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                return null;
            }
            Cookie acceeTokenCookie = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("access-token"))
                    .findFirst()
                    .orElse(null);
            log.info("토큰 정보 -> "+acceeTokenCookie.getName());
            if (acceeTokenCookie == null) {
                return null;
            }
            bearerToken = acceeTokenCookie.getValue();
        }
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }else if(StringUtils.hasText(bearerToken))
            return bearerToken;
        return null;
    }
}
