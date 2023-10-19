package com.example.dashboard.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dashboard.Entity.Account;
import com.example.dashboard.Entity.Transfer;
import com.example.dashboard.Repository.AccountRepository;
import com.example.dashboard.Repository.TransferRepository;



@Service
public class TransferServiceImpl implements TransferService{
    
    private TransferRepository transferRepository;
    private  AccountRepository accountRepository;
    
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> scheduledTask;

    public TransferServiceImpl(AccountRepository accountRepository,TransferRepository transferRepository){
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    //建立轉帳
    public ResponseEntity<String> createTransfer(Transfer transfer){
        
        //檢查 收款人存在
        Account recipientAccount = accountRepository.findById(transfer.getRecipientAccount().getId()).orElse(null);

        if (recipientAccount == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("recipient Not found");
        }

        //檢查 匯款人存在
        Account senderAccount = accountRepository.findById(transfer.getSenderAccount().getId()).orElse(null);

        if (senderAccount == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sender not found");
        }

        //取得匯款人帳戶金額
        BigDecimal senderAmount = senderAccount.getAmount();

        //取得匯款金額
        BigDecimal transferAmount = transfer.getTransferAmount();

        //如果帳戶金額小於匯款或是等於0，交易狀態為E
        if (transferAmount.compareTo(senderAmount) > 0 ) {

            transfer.setStatus("E"); //E == error
            transfer.setCreatedTime(Date.from(Instant.now()));
            transfer.setEndTime(Date.from(Instant.now()));
            transferRepository.save(transfer);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient funds");
            
        }else if (transferAmount.compareTo(BigDecimal.ZERO) == 0) {
            transfer.setStatus("E"); //E == error

            transfer.setCreatedTime(Date.from(Instant.now()));
            transfer.setEndTime(Date.from(Instant.now()));
            transferRepository.save(transfer);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("TransferAmount Cannot be zero.");
        
        }else{
           
            transfer.setStatus("P"); // P == Pending
            transfer.setCreatedTime(Date.from(Instant.now()));
            transfer.setEndTime(Date.from(Instant.now()));

            transferRepository.save(transfer);


            scheduledTask = scheduler.schedule(() -> {
                processingTransfer(transfer);
            }, 1, TimeUnit.MINUTES);

            return ResponseEntity.status(HttpStatus.OK).body("Transfer is pending and can be canceled within 5 minutes.");

        }
    }

    //處理轉帳
    private void processingTransfer(Transfer transfer) {

        //檢查 收款人存在
        Account recipientAccount = accountRepository.findById(transfer.getRecipientAccount().getId()).orElse(null);

       
        //檢查 匯款人存在
        Account senderAccount = accountRepository.findById(transfer.getSenderAccount().getId()).orElse(null);


        //取得匯款人金額
        BigDecimal senderAmount = senderAccount.getAmount();

        //取得匯款金額
        BigDecimal transferAmount = transfer.getTransferAmount();


        // 扣除匯款人的余额
        senderAmount = senderAmount.subtract(transferAmount);

        //取得收款人金額
        BigDecimal recipientAmount = recipientAccount.getAmount();

        //增加收款人的金額
        recipientAmount = recipientAmount.add(transferAmount);

        senderAccount.setAmount(senderAmount);
        recipientAccount.setAmount(recipientAmount);

        transfer.setStatus("S"); // s == Successfully
        transfer.setEndTime(Date.from(Instant.now()));

        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);

        transferRepository.save(transfer);
    }
    

    
    //取消轉帳
    public ResponseEntity<String> cancelTransfer(UUID Id){

        Transfer transfer = transferRepository.findByTransferId(Id);

        
        if(transfer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("TransferRecord Not found.");
        }

        //判斷交易狀態是P等待，才執行
        if(transfer.getStatus().equals("P")){

           
            if (scheduledTask != null) {
                scheduledTask.cancel(true); // 取消任务，不中断正在运行的任务
            }

            transfer.setStatus("C"); // C == cancel
            transfer.setCreatedTime(Date.from(Instant.now()));
            transfer.setEndTime(Date.from(Instant.now()));
            transferRepository.save(transfer);

            return ResponseEntity.status(HttpStatus.OK).body("Transfer Canceled Successfully."); 
        
        }else{
           
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The transfer has been processed and cannot be canceled.");
        }    
    }
}
