package Backend.MASJIB.filter;

import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MDCLoggingFilter extends GenericFilterBean { //멀티쓰레드 환경에서 MDC를 사용해 요청 별로 식별가능한 로그 남기기
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException { //MDC에 request_id를 넣어준다.
        final UUID uuid = UUID.randomUUID();
        MDC.put("request_id", uuid.toString());
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("request_id"); // MDC 클리어 대신에 해당 키를 제거하여 MDC를 유지함
            //MDC.clear();
        }
    }
}
