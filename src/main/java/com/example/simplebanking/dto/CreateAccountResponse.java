package com.example.simplebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountResponse
{
    private String owner;
    private String accountNumber;
    private String approvalCode;
    private double balance;
}
