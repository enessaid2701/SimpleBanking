package com.example.simplebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionListDTO
{
    private Date date;
    private String transactionType;
    private double amount;
    private String transactionMessage;
}
