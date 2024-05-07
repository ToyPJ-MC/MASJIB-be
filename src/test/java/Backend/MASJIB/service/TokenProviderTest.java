package Backend.MASJIB.service;

import Backend.MASJIB.member.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenProviderTest {
    private static final String AUTHORITIES_KEY = "roles";

    private static String secretKey ="f4a0b60a561086b6185205518d6962403e08507cb362e78b4f1bb6da4809cb05996a86d43c6a6a0924741ee8db0798a78e1e477fd6252e7323ea3cb0682f0aebb7108351dad4a57ebdec21f930ba7f993b75bc622fd4e34053dee676806192ebf7fd061572f9425b30fa85ec8406e5d013526841aaba472f1bcd9ad2228afa99";
    private final Long validityInMilliseconds =3600L;
    @Test
    @DisplayName("토큰 생성 테스트")
    void 토큰_생성_테스트(){
        long now = new Date().getTime();
        Date vaildity = new Date(now + validityInMilliseconds*1000*24*30);

        String jwt = Jwts.builder()
                .setHeaderParam("typ","JWT")
                .claim(AUTHORITIES_KEY, Role.ROLE_USER)
                .setSubject("hi")
                .setIssuedAt(new Date())
                .setExpiration(vaildity)
                .signWith(generalKey())
                .compact();
        System.out.println(jwt);

        System.out.println(validateToken(jwt));
        System.out.println(getExpiredTokenClaims(jwt));

    }
    public SecretKey generalKey(){
        byte[] encodedKey =Decoders.BASE64URL.decode(secretKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length,
                "HmacSHA512");
        return key;
    }
    public Boolean validateToken(String token){
        try{
            Jwts.parser().verifyWith(generalKey()).build().parseClaimsJws(token);
            return true;
        }catch (SignatureException e){ //signature가 에러

        }catch (MalformedJwtException e){ //토큰이 이상한 경우

        }catch (ExpiredJwtException e){ //토큰 만료인 경우

        }catch (UnsupportedJwtException e){ // 지원하지 않는 토큰

        }catch (IllegalArgumentException e){ //토큰이 비어있을 경우

        }
        return false;
    }
    public Claims getExpiredTokenClaims(String token){
        try{

            return Jwts.parser()
                    .verifyWith(generalKey())
                    .build()
                    .parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }
}
