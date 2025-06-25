package com.tangerineteam.gyoolworksap.controller;


import com.tangerineteam.gyoolworksap.dto.JwtToken;
import com.tangerineteam.gyoolworksap.dto.UserInfo;
import com.tangerineteam.gyoolworksap.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {


    @Autowired
    private UserService userService;

    @PostMapping("/test")
    public String test(){
        return "헤더에 토큰만 넣으면 된다`~~~~";
    }

    @PostMapping("/login")
    public JwtToken signin(@RequestBody UserInfo user) {

        JwtToken jwtToken = userService.signin(user);

        log.info("request userId = {}, password = {}", user.getId(), user.getPassword());
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());

        return  jwtToken;

    }
}
