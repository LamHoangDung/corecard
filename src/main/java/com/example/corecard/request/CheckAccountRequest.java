package com.example.corecard.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CheckAccountRequest {
    private String accountNumber;
    private String customerCode;
    private BigDecimal amount;
}
