package com.tangerineteam.gyoolworksap.controller;


import com.tangerineteam.gyoolworksap.common.ErrorResponse;
import com.tangerineteam.gyoolworksap.common.ResponseMassege;
import com.tangerineteam.gyoolworksap.dao.RedisTokenDao;
import com.tangerineteam.gyoolworksap.dto.JwtToken;
import com.tangerineteam.gyoolworksap.dto.UserInfo;
import com.tangerineteam.gyoolworksap.entity.UserEntity;
import com.tangerineteam.gyoolworksap.repository.UserRepository;
import com.tangerineteam.gyoolworksap.security.CookieService;
import com.tangerineteam.gyoolworksap.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
public class LoginController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTokenDao redisTokenDao;

    @Autowired
    private CookieService cookieService;


    @Value("${app.refresh-token-expiration-milliseconds}") // 10일
    private long jwtRefreshExpirationMs;

    @Value("${token.redis.prename}")
    private String prename;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserInfo user, HttpServletResponse response) {
        try {
            // 토큰 발급
            JwtToken jwtToken = userService.login(user);

            //1. cookie에 refresh 저장
            cookieService.addCookie(response,jwtToken.getRefreshToken());

            //2. redis에 refresh token 저장
            String redisKey= prename+user.getId();
            redisTokenDao.setValue(redisKey,jwtToken.getRefreshToken(), Duration.ofMillis(jwtRefreshExpirationMs));

            UserEntity userEntity =  userRepository.findById(user.getId()).orElseThrow();

            UserInfo userInfo = UserInfo.builder()
                    .id(userEntity.getId())
                    .name(userEntity.getName())
                    .email(userEntity.getEmail())
                    .role(String.join(",", userEntity.getRoles()))
                    .build();

            log.info(userInfo.toString());
            Map<String, Object> map = new HashMap<>();
            map.put("user", userInfo);
            map.put("token", jwtToken.getAccessToken());

            return  ResponseEntity.ok(map);
        }catch (Exception e){
            log.error("login error");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ResponseMassege.BAD_CREDENTIALS));
        }

    }
}
