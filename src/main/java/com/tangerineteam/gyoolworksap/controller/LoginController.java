package com.tangerineteam.gyoolworksap.controller;


import com.tangerineteam.gyoolworksap.common.ResponseMassege;
import com.tangerineteam.gyoolworksap.dto.JwtToken;
import com.tangerineteam.gyoolworksap.dto.UserInfo;
import com.tangerineteam.gyoolworksap.entity.UserEntity;
import com.tangerineteam.gyoolworksap.repository.UserRepository;
import com.tangerineteam.gyoolworksap.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserInfo user) {

        try {
            JwtToken jwtToken = userService.login(user);
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
            return ResponseEntity.badRequest().body(ResponseMassege.BAD_CREDENTIALS.getMessage());
        }

    }
}
