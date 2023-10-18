package com.example.dashboard.Service;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dashboard.Entity.User;
import com.example.dashboard.Security.JwtUtils;
import com.example.dashboard.Repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
    
   
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }


    //user註冊驗證 函數
    private void validateUser(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw new IllegalStateException("Email already exists");
        }

        if (user.getName() == null || user.getPassword() == null || user.getEmail() == null) {
            throw new IllegalArgumentException("Name, Password, Email, and Role are required");
        }
    
        if (user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }

    @Override
    public ResponseEntity<String> signup(User user) {

        try {
            
            validateUser(user);

            String hashPassword = hashPassword(user.getPassword());   

            user.setPassword(hashPassword);
            

            User result = userRepository.save(user);

            if(result!=null){
                return ResponseEntity.status(HttpStatus.OK).body("User SignUp Successfully");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signup failed");
            }

        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred during signup");
        }
    }


    @Override
    public ResponseEntity<String> signin(User user) {
        try {
            User userFromDB = userRepository.findByNameOrEmail(user.getName(), user.getEmail());
            if(userFromDB == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }

            if(passwordEncoder.matches(user.getPassword(),userFromDB.getPassword())){
               
                String token = JwtUtils.generateToken(userFromDB.getId(),userFromDB.getName(),userFromDB.getRole());
                return ResponseEntity.status(HttpStatus.OK).body(token);

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred during login");
        }
    }

    
}
