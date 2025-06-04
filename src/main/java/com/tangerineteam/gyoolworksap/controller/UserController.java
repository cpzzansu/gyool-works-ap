package com.tangerineteam.gyoolworksap.controller;


import com.tangerineteam.gyoolworksap.dto.UserInfo;
import com.tangerineteam.gyoolworksap.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserInfo user) {
        log.info(user.toString());

        userService.addUser(user);
        return ResponseEntity.ok("");
    }

    @PostMapping("/confirmEmail")
    public String confirmEmail(@RequestBody UserInfo user) {
        return "confirmEmail";
    }
}
