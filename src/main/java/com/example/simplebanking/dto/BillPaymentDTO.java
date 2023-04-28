package com.example.simplebanking.dto;

import com.example.simplebanking.model.BillPaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillPaymentDTO
{
    private String payee;
    private double amount;

    @Enumerated(EnumType.STRING)
    private BillPaymentStatus billPaymentStatus;

}
