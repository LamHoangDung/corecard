package com.example.corecard.response.Corebank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoreBankAccountData {
    private String accountNumber;
    private CoreBankCustomerData customer;
}