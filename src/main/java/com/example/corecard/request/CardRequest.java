package com.example.corecard.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class CardRequest {
    private String cardNumber;
    private BigDecimal amount;
    private String cvv;

}
