package com.example.dashboard.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dashboard.Entity.Account;
import com.example.dashboard.Service.AccountService;

import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@Tag(name="Account")
@RequestMapping("/api/accounts")
public class AccountController {
    
    private AccountService userService;

    public AccountController(AccountService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> SignUp(@RequestBody Account user){
        return userService.signup(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> SignIn(@RequestBody Account user){
        return userService.signin(user);
    }
}
