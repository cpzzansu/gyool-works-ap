package com.tangerineteam.gyoolworksap.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //토큰 생성/검증/authentication 변환등 로직 담당
    private final JwtProvider jwtProvider;

    //요청마다 실행되지만 OncePerRequestFilter에의해
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        //1. Authorizarion 헤더에서 토큰값 추출
        String token = resolveToken(request);

        //2. 토큰이 존재하고 유효 하면
        if( token != null && jwtProvider.validateToken(token) ) {
            //인증 정보 생성
            Authentication authentication = jwtProvider.getAuthentication(token);
            //SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 유효하지 않으면 401
        filterChain.doFilter(request, response);

    }

    /*
    *  Request에서 Authorization 헤더 읽고, "Bearer" 토큰만 추출
    * */
    private String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");

        // 헤더가 있고 "Bearer "로 시작하면 → 그 뒤 문자열만 반환
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length()); // "Bearer " 이후 문자열만 추출
        }

        return null;
    }
}
