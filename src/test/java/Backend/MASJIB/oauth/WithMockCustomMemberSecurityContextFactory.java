package Backend.MASJIB.oauth;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithMockCustomMemberSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomMember> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomMember customMember) {
        // 1
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // 2
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", customMember.email());
        // 3
        OAuth2User principal = new DefaultOAuth2User(
                List.of(new OAuth2UserAuthority(customMember.role(), attributes)),
                attributes,
                customMember.email());
        // 4
        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(
                principal,
                principal.getAuthorities(),
                customMember.registrationId());
        // 5
        context.setAuthentication(token);
        return context;
    }
}
