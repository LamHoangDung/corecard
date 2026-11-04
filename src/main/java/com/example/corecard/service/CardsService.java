package com.example.corecard.service;
import com.example.corecard.client.CoreBankingClient;
import com.example.corecard.client.CryptoClient;
import com.example.corecard.entity.CardsEntity;
import com.example.corecard.repository.CardsRepository;
import com.example.corecard.request.CheckAccountRequest;
import com.example.corecard.request.CryptoRequest;
import com.example.corecard.response.AccountInfoResponse;
import com.example.corecard.response.CheckAccountResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class CardsService {
    private CardsRepository cardsRepository;
    private CoreBankingClient coreBankingClient;
    private CryptoClient cryptoClient;

    public CardsService(CardsRepository cardsRepository, CoreBankingClient coreBankingClient, CryptoClient cryptoClient){
        this.coreBankingClient = coreBankingClient;
        this.cardsRepository = cardsRepository;
        this.cryptoClient = cryptoClient;
    }

    public String getCustomerCode(String cardNumberHash){
        try {
            CardsEntity card = new CardsEntity();
            card = cardsRepository.findByCardNumberHash(cardNumberHash);
            String customerCode = card.getCustomers().getCustomerCode();
            return customerCode;
        } catch (Exception e) {
            throw new RuntimeException("Loi khong tim thay customer code: " + e.getMessage());
        }
    }

    public AccountInfoResponse getAccountNumberAndCustomerCode(String cardNumberHash){
        try {
            CardsEntity card = cardsRepository.findByCardNumberHash(cardNumberHash);
            AccountInfoResponse accountInfoResponse = new AccountInfoResponse();
            accountInfoResponse.setAccountNumber(card.getAccountNumber());
            accountInfoResponse.setCustomerCode(card.getCustomers().getCustomerCode());
            return accountInfoResponse;
        }catch (Exception e){
            throw new RuntimeException("loi khong tim thay info account" + e.getMessage());
        }
    }

    public CheckAccountResponse checkCoreBankingAccount(String cardNumber, String cvv, BigDecimal amount){
        try {
            CryptoRequest cardNumberHashRequest = new CryptoRequest(cardNumber);
            CryptoRequest cvvHashRequest = new CryptoRequest(cvv);

            String cardNumberHash = cryptoClient.hashing(cardNumberHashRequest);
            String cvvHash = cryptoClient.hashing(cvvHashRequest);

            CardsEntity card = cardsRepository.findByCardNumberHashAndCvvHash(cardNumberHash,cvvHash);
            if (card == null) {
                return new CheckAccountResponse("invalid_card", false, "Thông tin thẻ không hợp lệ");
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
