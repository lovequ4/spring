package com.example.dashboard.Service;

import org.springframework.http.ResponseEntity;

import com.example.dashboard.Entity.Transfer;

public interface TransferService  {
    ResponseEntity<String> createTransfer(Transfer transfer);
}
