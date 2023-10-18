package com.example.dashboard.Service;

import org.springframework.http.ResponseEntity;

import com.example.dashboard.Entity.User;


public interface UserService {
    ResponseEntity<String> signup(User user);
    ResponseEntity<String> signin(User user);
}
