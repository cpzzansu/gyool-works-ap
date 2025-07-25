package com.tangerineteam.gyoolworksap.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tangerineteam.gyoolworksap.common.ErrorResponse;
import com.tangerineteam.gyoolworksap.common.ResponseMassege;
import com.tangerineteam.gyoolworksap.dao.RedisTokenDao;
import com.tangerineteam.gyoolworksap.dto.JwtToken;
import com.tangerineteam.gyoolworksap.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;


@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

//    //토큰 생성/검증/authentication 변환등 로직 담당
//    private final JwtProvider jwtProvider;
//
//    //refresh 관련 Cookie service
//    private final CookieService cookieService;
//
//    //redis 관련
//    private final RedisTokenDao redisTokenDao;;


    private final JwtProvider jwtProvider;
    private final CookieService cookieService;
    private final RedisTokenDao redisTokenDao;
    private final CustomerDetailService customerDetailService;

    public JwtAuthenticationFilter(
            JwtProvider jwtProvider,
            CookieService cookieService,
            RedisTokenDao redisTokenDao,
            CustomerDetailService customerDetailService
    ) {
        this.jwtProvider = jwtProvider;
        this.cookieService = cookieService;
        this.redisTokenDao = redisTokenDao;
        this.customerDetailService = customerDetailService;
    }

    @Value("${app.refresh-token-expiration-milliseconds}")
    private long refreshTokenExpirationMilliseconds;

    //요청마다 실행되지만 OncePerRequestFilter에의해
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        //1. Authorizarion 헤더에서 토큰값 추출
        String token = resolveToken(request);
        try {
            //2. 토큰이 존재하고 유효 하면
            if( token != null && jwtProvider.validateToken(token) ) {
                //인증 정보 생성
                Authentication authentication = jwtProvider.getAuthentication(token);
                //SecurityContext에 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            //2-1. 토큰이 만료된경우 refresh 토큰 확인 후 재발급
            else if(token != null && jwtProvider.isExpired(token)) {
                //cookie에서 refresh 토큰 가져오기
                String refreshToken = cookieService.getRefreshTokenCookie(request);
                String id = jwtProvider.getUsernameFromToken(refreshToken);
                String redisKey= "refresh-token-"+id;

                //redis에서 refresh 토큰 가져오기
                String  refreshTokenRedis  = redisTokenDao.getValue(redisKey);

                //refresh 만료 or 탈취 or 로그아웃
                if(refreshTokenRedis ==null || !refreshTokenRedis.equals(refreshToken)) {
                    cookieService.deleteCookie(response);
                    redisTokenDao.delete(redisKey);
                    SecurityContextHolder.clearContext();
                    sendErrorResponse(response, ResponseMassege.LOGGED_OUT); // 프론트는 여기서 로그인 페이지로 리디렉션
                    return;
                }

                // refresh로
                //access token 재발급
                String userId =  jwtProvider.getUsernameFromToken(refreshToken);
                UserDetails userDetails = customerDetailService.loadUserByUsername(userId);

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // 새 Access Token 발급해서 응답 헤더에 실어주기
                JwtToken newToken = jwtProvider.generateToken(authentication);

                String newAccessToken = newToken.getAccessToken();
                String newRefreshToken = newToken.getRefreshToken();

                cookieService.addCookie(response,newRefreshToken);
                redisTokenDao.setValue(redisKey,newRefreshToken,  Duration.ofMillis(refreshTokenExpirationMilliseconds));

                response.setHeader("Authorization", "Bearer " + newAccessToken);

                log.info("reissued token Uesr ID ==== >{}",id);

                // SecurityContext 갱신
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        }catch (Exception ex){
            log.error("Exception in JwtAuthenticationFilter ",ex);
        }

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


    private void sendErrorResponse(HttpServletResponse response, ResponseMassege rm) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse error = new ErrorResponse(rm);
        String json = new ObjectMapper().writeValueAsString(error);

        response.getWriter().write(json); // JSON 응답 본문에 쓰기
    }
}
