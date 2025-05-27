package com.tangerineteam.gyoolworksap.controller;


import com.tangerineteam.gyoolworksap.dto.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController {

    @PostMapping("/join")
    public String join(@RequestBody UserInfo user) {
        log.info(user.toString());
        return "join";
    }
}
