package com.example.dashboard.Service;

import java.math.BigDecimal;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.http.ResponseEntity;

import com.example.dashboard.Entity.Account;


public interface AccountService {
    ResponseEntity<String> signup(Account user);
    ResponseEntity<String> signin(Account user);
    BigDecimal getAccountAmount(long Id) throws AccountNotFoundException;
}
