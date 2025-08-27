package com.cocoh.movie.config.jwt;

import com.cocoh.movie.Entity.User;
import com.cocoh.movie.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private final SecretKey key;
    private final JwtParser parser;

    public TokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        // properties에 있는 secret-key를 불러오고 Base64를 디코딩 하여 Byte로 변환 후 HMAC SHA 키 객체를 생성
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecretKey()));
        // jwt 서명을 검증하고 사용자 아이디, 권한, 만료 시간 같은 구조화된 데이터를 추출
        parser = Jwts.parser().verifyWith(key).build();
    }

    public String generateToken(User user, Duration expiredAt, boolean isAccessToken) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiredAt.toMillis());

        List<String> authorities = user.getRole().getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .header().add("type", "JWT").add("alg", "HS256").and()
                .claims()
                    // properties 파일에 있는 토큰 발급자(Issuer)
                    .issuer(jwtProperties.getIssuer())
                    .issuedAt(now)
                    .expiration(expiry)
                    // 교유 식별자(유저 아이디)
                    .subject(user.getUsername())
                    .add("id", user.getId())
                    // Access 토큰인지, Refresh 토큰인지 구분
                    .add("type", isAccessToken ? "A" : "R")
                    .add("authorities", authorities)
                    .and()
                .signWith(key, Jwts.SIG.HS256)
                // JWT 문자열로 직렬화
                .compact();
    }

    public Authentication getAuthentication(String token) throws JwtException, IllegalArgumentException {
        // AccessToken을 이용해서 User type, id 등의 값을 가져옴
        Claims claims = getClaims(token);

        // RefreshToken은 인증용으로 사용할 수 없다.
        String type = claims.get("type").toString();
        if(type==null || !claims.get("type").equals("A")) throw new IllegalArgumentException((""));

        List<String> roles = claims.get("authorities", List.class);
        List<? extends GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();


        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(claims.getSubject()) // 토큰에서 추출한 사용자 아이디
                .password("") // 비밀번호는 사용 X(빈 문자열)
                .authorities(authorities) // 위에서 만든 user 권한 추가
                .build();

        // 생성한 userDetails, token, 권한 정보를 이용해서 Spring Security 인증 객체 반환
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    // jwt token에서 Claim(사용자 정보 등)을 추출하는 메서드
    public Claims getClaims(String token) throws JwtException, IllegalArgumentException {
        Jws<Claims> jws = parser.parseSignedClaims(token);
        return jws.getPayload();
    }

}
