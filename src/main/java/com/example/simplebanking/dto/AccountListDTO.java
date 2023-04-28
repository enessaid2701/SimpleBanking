package com.example.simplebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountListDTO
{
    private long id;
    private String owner;
    private String accountNumber;
    private double balance;
}
