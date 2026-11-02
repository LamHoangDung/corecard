package com.example.corecard.service;


import com.example.corecard.client.CoreBankingClient;
import com.example.corecard.entity.CardsEntity;
import com.example.corecard.repository.CardsRepository;
import com.example.corecard.request.CheckAccountRequest;
import com.example.corecard.response.AccountInfoResponse;
import com.example.corecard.response.BaseResponse;
import com.example.corecard.response.CheckAccountResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class CardsService {
    private CardsRepository cardsRepository;
    private CoreBankingClient coreBankingClient;

    public CardsService(CardsRepository cardsRepository, CoreBankingClient coreBankingClient){
        this.coreBankingClient = coreBankingClient;
        this.cardsRepository = cardsRepository;
    }

    public String getCustomerCode(String cardNumber){
        try {
            CardsEntity card = new CardsEntity();
            card = cardsRepository.findByCardNumber(cardNumber);
            String customerCode = card.getCustomers().getCustomerCode();
            return customerCode;
        } catch (Exception e) {
            throw new RuntimeException("Loi khong tim thay customer code: " + e.getMessage());
        }
    }

    public AccountInfoResponse getAccountNumberAndCustomerCode(String cardNumber){
        try {
            CardsEntity card = cardsRepository.findByCardNumber(cardNumber);
            AccountInfoResponse accountInfoResponse = new AccountInfoResponse();
            accountInfoResponse.setAccountNumber(card.getAccountNumber());
            accountInfoResponse.setCustomerCode(card.getCustomers().getCustomerCode());
            return accountInfoResponse;
        }catch (Exception e){
            throw new RuntimeException("loi khong tim thay info account" + e.getMessage());
        }
    }

    public CheckAccountResponse checkCoreBankingAccount(String cardNumber, BigDecimal amount){
        try {
            CardsEntity card = cardsRepository.findByCardNumber(cardNumber);
            if(card == null){
                return new CheckAccountResponse("card_not_found",false,"khong tim thay the tuong ung");
            }
            CheckAccountRequest checkAccountRequest = new CheckAccountRequest();
            checkAccountRequest.setAccountNumber(card.getAccountNumber());
            checkAccountRequest.setCustomerCode(card.getCustomers().getCustomerCode());
            checkAccountRequest.setAmount(amount);
            return coreBankingClient.checkAccount(checkAccountRequest);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
