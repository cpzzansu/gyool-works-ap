package com.tangerineteam.gyoolworksap.controller;


import com.tangerineteam.gyoolworksap.dto.UserInfo;
import com.tangerineteam.gyoolworksap.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserInfo user) {
        log.info(user.toString());

        userService.addUser(user);
        return ResponseEntity.ok("");
    }

    @PostMapping("/duplication")
    public ResponseEntity<?> duplication(@RequestBody UserInfo user) {

        boolean isRegister = !userService.isExistUserId(user);

        return ResponseEntity.ok(isRegister);
    }

    @PostMapping("/confirmEmail")
    public String confirmEmail(@RequestBody UserInfo user) {
        log.info(user.toString());
        return "confirmEmail";
    }
}
