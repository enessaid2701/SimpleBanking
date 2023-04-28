package com.example.simplebanking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class BillPayment extends BaseModel
{
    private String paye;
    private double amount;

    @Enumerated(EnumType.STRING)
    private BillPaymentStatus billPaymentStatus;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Account account;
}
