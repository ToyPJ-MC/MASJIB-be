package Backend.MASJIB.jwt.service;

import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.member.entity.Role;
import Backend.MASJIB.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class CustomOauth2Service extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    public CustomOauth2Service(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User= super.loadUser(userRequest);
        Map<String, Object> attributes =oAuth2User.getAttributes();

        List<GrantedAuthority> authorities = new ArrayList<>(); // SimpleGrantedAuthority는 GrantedAuthority 구현체 중 하나
        //AuthorityUtils.createAuthorityList("ROLE_GUEST"); // 권한을 ROLE_USER로 설정
        String userNameAttributeName = userRequest.getClientRegistration() // registrationId로 어떤 OAuth로 로그인 했는지 확인합니다.
                .getProviderDetails() // OAuth2 로그인 진행 시 키가 되는 필드값을 이야기합니다. Primary Key와 같은 의미입니다.
                .getUserInfoEndpoint() // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당합니다.
                .getUserNameAttributeName();  // userNameAttributeName은 OAuth2 로그인 진행 시 키가 되는 필드값을 이야기합니다. Primary Key와 같은 의미입니다.

        Map<String,Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = kakaoAccount.get("email").toString();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if(!memberRepository.existsByEmail(email)){
            Member member = new Member();
            member.setEmail(email);
            member.setRole(Role.ROLE_USER);
            member.setNickname(setUserNicNameByUUID());
            member.setCreateTime(LocalDateTime.now());

            memberRepository.save(member);
        }
        return new DefaultOAuth2User(authorities,attributes,userNameAttributeName);//OAuth2User를 구현한 DefaultOAuth2User를 반환합니다.
    }

    private String setUserNicNameByUUID(){
        String nickname = "@user-";
        String random = UUID.randomUUID().toString();
        random = random.replaceAll("-", "");
        random = random.substring(0,7);
        nickname+= random;
        return nickname;
    }
}
