package com.example.dashboard.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
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
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    
    public TransferServiceImpl(AccountRepository accountRepository,TransferRepository transferRepository){
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

   
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

        //取得匯款人金額
        BigDecimal senderAmount = senderAccount.getAmount();

        //取得匯款金額
        BigDecimal transferAmount = transfer.getTransferAmount();

        
        if (transferAmount.compareTo(senderAmount) > 0) {
            transfer.setStatus("E"); //E == error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient funds");
        }else{
           
            transfer.setStatus("P"); // P == Pending
            transfer.setCreatedTime(Date.from(Instant.now()));
            transfer.setEndTime(Date.from(Instant.now()));

            transferRepository.save(transfer);

            // 使用AtomicReference包裝response可讓您在Lambda表達式內部修改其值，
            // 解決"Local variable initialResponse defined in an enclosing scope must be final or effectively final"的問題。
            AtomicReference<ResponseEntity<String>> responseRef = new AtomicReference<>(ResponseEntity.ok("Transfer is pending and can be canceled within 5 minutes."));
    
            executorService.schedule(() -> {

                ResponseEntity<String> processingResponse = processingTransfer(transfer);
                
                if (processingResponse != null) {
                    responseRef.set(processingResponse);
                }

            }, 5, TimeUnit.MINUTES);

            return responseRef.get();

        }
    }

    
    private ResponseEntity<String> processingTransfer(Transfer transfer) {

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

        return ResponseEntity.ok("Transfer Successfully.");
    }
    
}
