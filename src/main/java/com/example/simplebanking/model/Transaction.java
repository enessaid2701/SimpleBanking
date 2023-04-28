package com.example.simplebanking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
public class Transaction extends BaseModel
{
    private String transactionMessage;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    public double amount;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Account account;
}
