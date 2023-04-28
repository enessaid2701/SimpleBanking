package com.example.simplebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAccountResponse
{
    private String owner;
    private String accountNumber;
    private double balance;
}
