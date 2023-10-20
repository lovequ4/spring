package com.example.dashboard.DTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class TransferRecordsDTO {
    private UUID transferId;
   
    private Long senderAccountId; 

    private BigDecimal transferAmount; 

    private Long recipientAccountId; 

    private String description; 

    private Date createdTime; 

    private Date endTime; 

    private String status; 

    public String getStatus() {
        if ("P".equals(status)) {
            return "Padding";
        } else if ("S".equals(status)) {
            return "Successfully";
        } else if ("C".equals(status)) {
            return "Cancel";
        } else if ("E".equals(status)){
            return "Error";
        } else{
            return "Unknown status";
        }
    }
}

