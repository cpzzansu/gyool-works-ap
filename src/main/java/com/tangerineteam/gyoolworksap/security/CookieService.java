package com.tangerineteam.gyoolworksap.security;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CookieService {

    @Value("${app.refresh-token-expiration-milliseconds}") // 10일
    private long jwtRefreshExpirationMs;

    @Value("${token.cookie.name}")
    private String refreshTokenCookieName;


    //  쿠키에 refresh 토큰 저장
    public void addCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from(refreshTokenCookieName, refreshToken)
                .httpOnly(true)
                .secure(false) // ✅ 개발환경에서는 false, 배포 시 true
                .path("/")
                .maxAge(Duration.ofMillis(jwtRefreshExpirationMs))
                .sameSite("Lax") // ✅ SameSite=Lax 설정
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    }


    // 쿠키에 저장된 refresh 토큰 가져오기
    public String getRefreshTokenCookie(HttpServletRequest request) {
        String refreshToken = null;

        if(request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (refreshTokenCookieName.equals(cookie.getName())) {
                    return  cookie.getValue();
                }
            }
        }

        return  refreshToken;
    }


    //쿠키에 저장된 refresh 토큰삭제
    public void deleteCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(refreshTokenCookieName, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);  //즉시만료로 삭제 진행
        response.addCookie(cookie);

    }
}
