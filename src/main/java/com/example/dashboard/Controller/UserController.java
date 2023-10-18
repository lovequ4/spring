package com.example.dashboard.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dashboard.Entity.User;
import com.example.dashboard.Service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@Tag(name="User")
@RequestMapping("/api/users")
public class UserController {
    
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> SignUp(@RequestBody User user){
        return userService.signup(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> SignIn(@RequestBody User user){
        return userService.signin(user);
    }
}
