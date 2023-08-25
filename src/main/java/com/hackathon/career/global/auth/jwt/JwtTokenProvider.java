package com.hackathon.career.global.auth.jwt;


import com.hackathon.career.global.exception.GlobalException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public TokenInfo generateToken(Authentication authentication, HttpServletResponse response) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 86400000);//1일
//        Date accessTokenExpiresIn = new Date(now + 1000*60);//1분

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + 1209600000))//2주
//                .setExpiration(new Date(now + 1000*60*5))//2주
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
//{
//    "grantType": "Bearer",
//    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxcTFhc2ZkYXExcTFxIiwiYXV0aCI6IiIsImV4cCI6MTY5MTU0MjYyNH0.fD1lnJ2Yl67zSluFd5jKCwddcl9i3-pPgsUSuySriIA",
//    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2OTE1NDI4NjR9.RVsKk4JZS65QlBjuBUXGYFzc0G2UJAnvS4MpF98DXfQ"
//}

//{
//    "grantType": "Bearer",
//    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxcTFhc2ZkYXExcTFxIiwiYXV0aCI6IiIsImV4cCI6MTY5MTU0MjY5OH0.2850rdQ1WiblLuni26Z0tejt9jNngOg3_fMkU3VpdN8",
//    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2OTE1NDI5Mzh9.RSLdm0FarLyuolIIOCZzXuhjB5KZ5U0HZ3JDWcrcBS4"
//}3


        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 정보를 검증하는 메서드
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT Token", e);
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT Token", e);
            throw new GlobalException(HttpStatus.UNAUTHORIZED, "Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token", e);
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.", e);
            throw new GlobalException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}