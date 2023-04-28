package com.example.simplebanking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Entity
@Data
public class Account extends BaseModel
{
    private String owner;
    private String accountNumber;
    private double balance;
    private String approvalCode;
}
