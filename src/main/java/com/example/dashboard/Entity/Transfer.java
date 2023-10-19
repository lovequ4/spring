package com.example.dashboard.Entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="transferRecord")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {
    
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID transferId;

    @ManyToOne
    @JoinColumn(name = "sender_account_id",nullable = false)
    private Account senderAccount;

    @Column(nullable = false)
    private BigDecimal  transferAmount;

    @ManyToOne
    @JoinColumn(name = "recipient_account_id",nullable = false)
    private Account recipientAccount;

    @Column
    private String description;

    @Column(nullable = false)
    private Date createdTime;

    @Column(nullable = false)
    private Date endTime;

    @Column(nullable = false)
    private String status;
}
