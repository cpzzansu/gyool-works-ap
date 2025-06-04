package com.tangerineteam.gyoolworksap.security;

import com.tangerineteam.gyoolworksap.config.SecurityConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestUri = request.getRequestURI();

        // SecurityConfig.AUTH_WHITELIST에 선언된 패턴 중 하나라도 일치하면 필터를 skip
        for (String pattern : SecurityConfig.AUTH_WHITELIST) {
            if (pathMatcher.match(pattern, requestUri)) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1) Authorization 헤더에서 Bearer 토큰(resolve
        String token = getTokenFromRequest(request);
//
//        // 2) 토큰 검증
//        if (hasText(token) && jwtTokenProvider.validateToken(token)) {
//            // 2-1) 토큰에서 사용자 이름(Username) 가져오기
//            String username = jwtTokenProvider.getUsername(token);
//
//            // 2-2) UserDetailsService를 통해 UserDetails 로드
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//            // 2-3) Authentication 객체 생성 후 SecurityContext에 세팅
//            UsernamePasswordAuthenticationToken authenticationToken =
//                    new UsernamePasswordAuthenticationToken(
//                            userDetails,
//                            null,
//                            userDetails.getAuthorities()
//                    );
//            authenticationToken.setDetails(
//                    new WebAuthenticationDetailsSource().buildDetails(request)
//            );
//
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        }
//

        // 3) 다음 필터/서블릿 체인으로 요청 전달
        filterChain.doFilter(request, response);
    }


    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
