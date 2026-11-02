package com.example.corecard.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CardRequest {
    private String cardNumber;
    private BigDecimal amount;

}
