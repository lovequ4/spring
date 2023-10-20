package com.example.dashboard.Controller;

import java.math.BigDecimal;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dashboard.Entity.Account;
import com.example.dashboard.Service.AccountService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@Tag(name="Account")
@RequestMapping("/api/accounts")
public class AccountController {
    
    private AccountService accountService;

    public AccountController(AccountService userService) {
        this.accountService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> SignUp(@RequestBody Account user){
        return accountService.signup(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> SignIn(@RequestBody Account user){
        return accountService.signin(user);
    }

    @GetMapping("/{Id}")
    @Parameter(
        name = "Authorization",
        description = "JWT token",
        in = ParameterIn.HEADER,
        example = "Bearer ",
        required = true
    )
    public BigDecimal getAccountAmount(@PathVariable long Id) throws AccountNotFoundException{
        return accountService.getAccountAmount(Id);
    }
}
