package com.example.corecard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "transaction_status")
public class TransactionStatusEntity {
    @Id
    private String code;
    private String name;
}
