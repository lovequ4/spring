package com.example.dashboard.Service;


import java.math.BigDecimal;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dashboard.Entity.Account;
import com.example.dashboard.Security.JwtUtils;
import com.example.dashboard.Repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService{
    
   
    private AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder){
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }


    //account註冊驗證 函數
    private void validateAccount(Account account){
        if(accountRepository.existsByEmail(account.getEmail())){
            throw new IllegalStateException("Email already exists");
        }

        if (account.getName() == null || account.getPassword() == null || account.getEmail() == null) {
            throw new IllegalArgumentException("Name, Password, Email, and Role are required");
        }
    
        if (account.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }

    @Override
    public ResponseEntity<String> signup(Account account) {

        try {
            
            validateAccount(account);

            String hashPassword = hashPassword(account.getPassword());   

            account.setPassword(hashPassword);
            

            Account result = accountRepository.save(account);

            if(result!=null){
                return ResponseEntity.status(HttpStatus.OK).body("Account SignUp Successfully");
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
    public ResponseEntity<String> signin(Account account) {
        try {
            Account accountFromDB = accountRepository.findByNameOrEmail(account.getName(), account.getEmail());
            if(accountFromDB == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("account not found");
            }

            if(passwordEncoder.matches(account.getPassword(),accountFromDB.getPassword())){
               
                String token = JwtUtils.generateToken(accountFromDB.getId(),accountFromDB.getName(),accountFromDB.getRole());
                return ResponseEntity.status(HttpStatus.OK).body(token);

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred during login");
        }
    }

    
    public BigDecimal getAccountAmount(long Id)throws AccountNotFoundException{
    
        Account account = accountRepository.findById(Id);

        if (account != null) {
            return account.getAmount();
        } else {
            throw new AccountNotFoundException("Account not found for Id: " + Id);
        }
    }
}
