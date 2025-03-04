package com.user.microservice.controller;

import com.user.microservice.domains.User;
import com.user.microservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(user));
    }

    @GetMapping("/valid")
    public ResponseEntity<String> valideCode(@RequestParam int code) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.validCode(code));
    }

    @GetMapping("/email")
    public ResponseEntity<String> valideCode(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(email));
    }

    @GetMapping
    public ResponseEntity<List<User>> listAll() {
        List<User> list = userService.listAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
}
