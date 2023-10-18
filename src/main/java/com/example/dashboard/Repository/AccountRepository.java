package com.example.dashboard.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dashboard.Entity.Account;

public interface AccountRepository extends JpaRepository<Account,Long>{
    Account findByNameOrEmail(String name, String email);
    Account findById(long Id);
    boolean existsByEmail(String email);
}
