package com.example.dashboard.Repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dashboard.Entity.Transfer;



public interface TransferRepository extends JpaRepository<Transfer,Long>{
    Transfer findByTransferId(UUID Id);
    List<Transfer> findBySenderAccountId(long Id);
}
