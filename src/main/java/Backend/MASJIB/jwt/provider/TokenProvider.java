package Backend.MASJIB.jwt.provider;

import Backend.MASJIB.jwt.dto.ResponseTokenDto;
import Backend.MASJIB.jwt.redis.RedisUtil;
import Backend.MASJIB.member.entity.Member;
import Backend.MASJIB.member.entity.Role;
import Backend.MASJIB.member.repository.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "roles";
    private final String secretKey;
    private final Long validityInMilliseconds;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;

    public TokenProvider(@Value("${jwt.secret}")String secretKey,@Value("${jwt.expire_in_seconds}")Long validityInMilliseconds, MemberRepository memberRepository, RedisUtil redisUtil) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
        this.memberRepository = memberRepository;
        this.redisUtil = redisUtil;
    }

    public String createToken(String email){
        long now = new Date().getTime();
        Date vaildity = new Date(now + validityInMilliseconds*1000); //1시간

        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .claim(AUTHORITIES_KEY, Role.ROLE_USER)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(vaildity)
                .signWith(generalKey(),SignatureAlgorithm.HS512)
                .compact();
    }
    public String createRefreshToken(String email){
        long now = new Date().getTime();
        Date vaildity = new Date(now + validityInMilliseconds*1000*24*30);

        String token =Jwts.builder()
                .issuedAt(new Date())
                .subject(email)
                .issuer("masjib")
                .expiration(vaildity)
                .claim(AUTHORITIES_KEY, Role.ROLE_USER)
                .signWith(generalKey())
                .compact();
        System.out.println(token);
        redisUtil.setTokenToRedis(email+"_refreshToken",token,validityInMilliseconds*1000*24*30);
        return token;
    }
    public ResponseTokenDto reGenerateToken(String refreshToken){

        if(!validateToken(refreshToken) || refreshToken ==null) throw new RuntimeException("유효하지 않은 토큰입니다.");
        String email = getExpiredTokenClaims(refreshToken).getSubject();

        Optional<String> findRedisToRefreshToken = redisUtil.getTokenToRedis(email+"_refreshToken");
        if(findRedisToRefreshToken.isEmpty()) throw new RuntimeException("빈토큰입니다.");

        if(!findRedisToRefreshToken.get().equals(refreshToken)) throw new RuntimeException("일치하지 않은 토큰입니다.");

        Optional<Member> member = memberRepository.findByEmail(email);
        member.orElseThrow(RuntimeException::new);

        String newAccessToken = createToken(email);
        String newRefreshToken = createRefreshToken(email);

        ResponseTokenDto dto = new ResponseTokenDto();
        dto.setAccessToken(newAccessToken);
        dto.setAccessTokenExpiresIn(validityInMilliseconds*1000);
        dto.setRefreshToken(newRefreshToken);
        dto.setRefreshTokenExpiresIn(validityInMilliseconds*1000*24*30);
        return dto;

    }
    public SecretKey generalKey(){
        byte[] encodedKey =Decoders.BASE64URL.decode(secretKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length,
                "HmacSHA512");
        return key;
    }
    public Boolean validateToken(String token){
        try{
            Jwts.parser().verifyWith(generalKey()).build().parseSignedClaims(token);
            return true;
        }catch (SignatureException e){ //signature가 에러
            System.out.println(e.getMessage());
        }catch (MalformedJwtException e){ //토큰이 이상한 경우
            System.out.println("malformed error");
        }catch (ExpiredJwtException e){ //토큰 만료인 경우
            System.out.println("expired error");
        }catch (UnsupportedJwtException e){ // 지원하지 않는 토큰
            System.out.println("unsupported error");
        }catch (IllegalArgumentException e){ //토큰이 비어있을 경우
            System.out.println("illegal error");
        }
        return false;
    }

    public Claims getExpiredTokenClaims(String token){
        try{
            return Jwts.parser()
                    .setSigningKey(generalKey())
                    .build()
                    .parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    public Authentication getAuthentication(String token){

        Claims claims = Jwts.parser()
                .setSigningKey(generalKey())
                .build()
                .parseClaimsJws(token)
                .getBody(); //토큰을 파싱해서 claims를 가져온다.
        List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList()); //권한을 가져온다.
        User principal = new User(claims.getSubject(),"",authorities); //principal은 인증된 사용자의 정보를 담고있는 객체
        return new UsernamePasswordAuthenticationToken(principal,token,authorities); //토큰, 권한을 담아서 Authentication 객체를 리턴
    }
    public String getEmail(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(generalKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

}
