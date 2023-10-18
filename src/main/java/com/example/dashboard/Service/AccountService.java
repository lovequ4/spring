package com.example.dashboard.Service;

import org.springframework.http.ResponseEntity;

import com.example.dashboard.Entity.Account;


public interface AccountService {
    ResponseEntity<String> signup(Account user);
    ResponseEntity<String> signin(Account user);
}
