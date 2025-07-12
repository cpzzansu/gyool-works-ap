package com.tangerineteam.gyoolworksap.security;


import com.tangerineteam.gyoolworksap.dao.RedisTokenDao;
import com.tangerineteam.gyoolworksap.dto.JwtToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {

    @Value("${app.jwt-secret}")
    private String secretKey;

    @Value("${app.jwt-expiration-milliseconds}") // 30분
    private long jwtExpirationDate;

    @Value("${app.refresh-token-expiration-milliseconds}") // 10일
    private long jwtRefreshExpirationMs;

    @Autowired
    private RedisTokenDao redisTokenDao;

    private SecretKey key;


    @PostConstruct
    public void init() {
        byte[] decoded = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(decoded);
    }

    public JwtToken generateToken(Authentication authentication){

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));


        String userName = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        //access token 생성
        String accessToken = Jwts.builder()
                .setSubject(userName)
                .claim("auth", authorities)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();


        //refresh token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(currentDate.getTime() + jwtRefreshExpirationMs))
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();

        // redis에 refresh token 저장
        String redisKey= "refresh-token-"+userName;
        redisTokenDao.setValue(redisKey,refreshToken, Duration.ofMillis(jwtRefreshExpirationMs));


        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    //토큰 정보 검증
    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

            return true;

        }catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }

        return false;
    }

    //jwt 토큰 복호화 후 토큰 정보 꺼내기
    public Authentication getAuthentication(String token){
        Claims claims = parseClaims(token);

        if(claims.get("auth") == null){
            throw new RuntimeException("권한정보가 없는 토큰입니다.");
        }

        //claims에서 권한 정보가져오기
        Collection<?extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new) //문자열을 SimpleGrantedAuthority 객체로 변환
                .collect(Collectors.toList()); //변환된 스트림을 다시 리스트로 모음

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // accessToken
    private Claims parseClaims(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }
}
