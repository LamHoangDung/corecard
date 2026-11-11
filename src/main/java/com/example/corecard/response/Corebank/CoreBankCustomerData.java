package com.example.corecard.response.Corebank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoreBankCustomerData {
    private String customerCode;
    private String fullName;
    private String phone;
    private String email;
}