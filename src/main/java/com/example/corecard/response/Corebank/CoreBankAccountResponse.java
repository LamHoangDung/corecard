package com.example.corecard.response.Corebank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoreBankAccountResponse {
    private String code;
    private String message;
    private CoreBankAccountData data;
}