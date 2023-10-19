package com.example.dashboard.Service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.example.dashboard.Entity.Transfer;

public interface TransferService  {
    ResponseEntity<String> createTransfer(Transfer transfer);
    ResponseEntity<String> cancelTransfer(UUID Id);
}
