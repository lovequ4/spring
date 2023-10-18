package com.example.dashboard.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dashboard.Entity.User;

public interface UserRepository extends JpaRepository<User,Long>{
    User findByNameOrEmail(String name, String email);
    User findById(long Id);
    boolean existsByEmail(String email);
}
