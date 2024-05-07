package Backend.MASJIB.util;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j
public class SecurityUtil {

    @Nullable
    public static String getLoginMemberEmail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || isAnonymous()) {
            log.debug("Security Context에 인증 정보 없습니다.");
            return null;
        }

        // 인증 객체에서 사용자 이메일을 가져오는데, Spring Security의 UserDetails 객체를 사용하고 있습니다.
        // 만약 인증 객체의 Principal이 UserDetails 객체인 경우, 해당 UserDetails 객체에서 사용자명을 가져와서 memberEmail 변수에 할당합니다.
        // 그렇지 않고 Principal이 String 객체인 경우, 해당 String을 memberEmail 변수에 할당합니다.

        String memberEmail = null;
        if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            memberEmail = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            memberEmail = (String) authentication.getPrincipal();
        }
        return memberEmail;
    }
    public static Optional<String> getCurrentMemberEmail() {
        return Optional.ofNullable(getLoginMemberEmail());
    }

    public static boolean isAnonymous() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.debug("Security Context에 인증 정보 없습니다.");
            return true;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ANONYMOUS"));
    }
}
