package com.example.corecard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity(name = "cards")
public class CardsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardNumber;
    private String accountNumber;
    private String cvv;
    private BigDecimal creditLimit;
    private LocalDateTime createdAt;
    private LocalDate expiredDate;

    @ManyToOne
    @JoinColumn(name = "activity_status")
    private ActivityStatusEntity activityStatus;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomersEntity customers;

    @ManyToOne
    @JoinColumn(name = "card_product_id")
    private CardProductEntity cardProduct;


}
