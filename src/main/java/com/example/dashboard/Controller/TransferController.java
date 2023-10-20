package com.example.dashboard.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dashboard.DTO.TransferRecordsDTO;
import com.example.dashboard.Entity.Transfer;
import com.example.dashboard.Service.TransferService;

import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@Tag(name="Transfer")
@RequestMapping("/api/transfer")
public class TransferController {
    
    private TransferService transferService;

    public TransferController(TransferService transferService){
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<String> CreateTransfer(@RequestBody Transfer transfer){
        return  transferService.createTransfer(transfer);
    }

    @PostMapping("/{Id}")
    public ResponseEntity<String> cancelTransfer(@PathVariable UUID Id) {
        return transferService.cancelTransfer(Id);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<List<TransferRecordsDTO>> getTransferRecord(@PathVariable long Id){
        return transferService.getTransferRecords(Id);
    }
}
