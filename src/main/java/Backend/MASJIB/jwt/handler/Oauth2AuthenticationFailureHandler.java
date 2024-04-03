package Backend.MASJIB.jwt.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class Oauth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        /*super.onAuthenticationFailure(request, response, exception);
        response.setStatus(401);
        response.sendError(401,exception.getMessage());*/
        // 이전에 sendError를 호출하지 않고, 401 상태 코드만 설정합니다.
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 예외 메시지를 확인하고, 비어 있지 않으면 응답에 메시지를 포함합니다.
        String errorMessage = exception.getMessage();
        if (StringUtils.hasText(errorMessage)) {
            response.getWriter().println(errorMessage);
            System.out.println(errorMessage);
        }
    }
}
