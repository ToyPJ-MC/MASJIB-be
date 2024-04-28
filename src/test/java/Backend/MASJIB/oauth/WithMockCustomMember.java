package Backend.MASJIB.oauth;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomMemberSecurityContextFactory.class)
public @interface WithMockCustomMember {

    String email() default "test@test.com";
    String role() default "ROLE_USER";
    String registrationId() default "kakao";
}
