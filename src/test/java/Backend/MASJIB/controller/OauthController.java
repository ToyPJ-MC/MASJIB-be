package Backend.MASJIB.controller;


import Backend.MASJIB.jwt.dto.ResponseTokenDto;
import Backend.MASJIB.jwt.filter.JwtFilter;
import Backend.MASJIB.jwt.handler.GlobalExceptionHandler;
import Backend.MASJIB.jwt.provider.TokenProvider;
import Backend.MASJIB.jwt.redis.RedisUtil;
import Backend.MASJIB.member.entity.Role;
import Backend.MASJIB.member.repository.MemberRepository;
import Backend.MASJIB.oauth.WithMockCustomMember;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

/*@AutoConfigureRestDocs
@WebMvcTest(OauthController.class)
public class OauthController {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TokenProvider tokenProvider;

    @Test
    @DisplayName("Oauth2 Login 성공 테스트")
    //@WithMockCustomMember(role = "ROLE_USER",registrationId = "kakao")
    void Oauth2로그인_테스트() throws Exception {
        String refreshToken = tokenProvider.createRefreshToken("test@test.com");
        System.out.println(refreshToken);
        // 리프레시 토큰을 사용하여 새로운 토큰을 생성하고 해당 DTO 객체를 반환하도록 설정
        ResponseTokenDto tokenDto = tokenProvider.reGenerateToken(refreshToken);
        System.out.println(tokenDto.getAccessToken());
        // reGenerateToken() 메서드가 호출될 때 해당 DTO 객체를 반환하도록 설정
        given(tokenProvider.reGenerateToken(refreshToken)).willReturn(tokenDto);

        // GET 요청을 수행하여 카카오 로그인 엔드포인트에 접근하도록 설정
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/oauth/refresh")
                        // 사용자의 로그인 정보와 권한을 설정하는 OAuth2 로그인을 사용하지 않음
                        .with(user("test@test.com").roles("USER"))
                        // 유효한 리프레시 토큰을 파라미터로 전달
                        .param("refreshToken", refreshToken)
                )
                // 응답 상태가 OK(200)임을 검증
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}*/
