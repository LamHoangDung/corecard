package com.example.corecard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity(name = "card_transactions")
public class CardTransactionsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private CardsEntity cards;

    private String requestId;
    private String accountNumber;
    private String merchantCode;
    private BigDecimal amount;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_status", nullable = false)
    private TransactionStatusEntity transactionStatus;

    private String authenticationReference;
    private String bankingTransactionCode;
}
