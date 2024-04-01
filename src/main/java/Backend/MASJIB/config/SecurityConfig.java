package Backend.MASJIB.config;


import Backend.MASJIB.jwt.filter.JwtFilter;
import Backend.MASJIB.jwt.handler.JwtAccessDeniedHandler;
import Backend.MASJIB.jwt.handler.JwtAuthenticationEntryPoint;
import Backend.MASJIB.jwt.handler.Oauth2AuthenticationFailureHandler;
import Backend.MASJIB.jwt.handler.Oauth2AuthenticationSuccessHandler;
import Backend.MASJIB.jwt.provider.TokenProvider;
import Backend.MASJIB.jwt.service.CustomOauth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomOauth2Service customOauth2Service;
    private final Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
    private final Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;
    private final JwtFilter jwtFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(CustomOauth2Service customOauth2Service, Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler,
                          Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler, JwtFilter jwtFilter, JwtAccessDeniedHandler jwtAccessDeniedHandler,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.customOauth2Service = customOauth2Service;
        this.oauth2AuthenticationSuccessHandler = oauth2AuthenticationSuccessHandler;
        this.oauth2AuthenticationFailureHandler = oauth2AuthenticationFailureHandler;
        this.jwtFilter = jwtFilter;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain Configure(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // csrf 보안 토큰 disable처리.
            .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 페이지 disable처리.
            .httpBasic(AbstractHttpConfigurer::disable) // httpBasic disable처리.
            .headers((header)->header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // X-Frame-Options disable처리.
            .oauth2Login((login)->login
                    .successHandler(oauth2AuthenticationSuccessHandler) // oauth2Login에서 로그인 성공 처리
                    .failureHandler(oauth2AuthenticationFailureHandler) // oauth2Login에서 로그인 실패 처리
                    .userInfoEndpoint((info)->info.userService(customOauth2Service)) // oauth2Login에서 사용할 CustomOauth2Service를 등록
            ) // oauth2Login 설정
            .exceptionHandling((exception)->exception
                    .accessDeniedHandler(jwtAccessDeniedHandler) // 인가가 안된 사용자가 접근할 때 처리
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증이 안된 사용자가 접근할 때 처리
            ) // 예외처리
            .sessionManagement((session)->session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않겠다.
            ) // 세션관리
            .authorizeHttpRequests((request)->request
                    //.requestMatchers("/api/review/**").hasAnyAuthority("ROLE_USER")
                    .requestMatchers("/api/review/**","/images/**","/swagger-ui/**","/v3/api-docs/**","api/shop/**","/swagger-resources/**","/webjars/**","/configuration/**","/v3/**","/v2/**").permitAll()
                    .requestMatchers("/api/oauth/login/**").permitAll()
                    .requestMatchers("/api/oauth/refresh/**").permitAll()
                    .requestMatchers("/docs/index.html").permitAll()
                    .requestMatchers("/api/upload/**").permitAll()
                    .anyRequest().authenticated()
            ); // 인증
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // jwtFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다.
        return http.build();
    }

}
