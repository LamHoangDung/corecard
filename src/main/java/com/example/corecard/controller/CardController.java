package com.example.corecard.controller;

import com.example.corecard.request.CardRequest;
import com.example.corecard.request.CheckAccountRequest;
import com.example.corecard.response.BaseResponse;
import com.example.corecard.response.CheckAccountResponse;
import com.example.corecard.service.CardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card")
public class CardController {
    private CardsService cardsService;

    public CardController (CardsService cardsService){
        this.cardsService = cardsService;
    }

    @PostMapping("/getCustomerCode")
    ResponseEntity<BaseResponse> getCustomerCode(@RequestBody CardRequest cardRequest){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode("200");
        baseResponse.setData(cardsService.getCustomerCode(cardRequest.getCardNumber()));
        baseResponse.setMessage("lay customercode thanh cong");
        return ResponseEntity.ok(baseResponse);
    }

    @PostMapping("/getAccountNumberAndCustomerCode")
    ResponseEntity<BaseResponse> getAccountNumberAndCustomerCode(@RequestBody CardRequest cardRequest){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode("200");
        baseResponse.setData(cardsService.getAccountNumberAndCustomerCode(cardRequest.getCardNumber()));
        baseResponse.setMessage("lay account info ");
        return ResponseEntity.ok(baseResponse);
    }

    @PostMapping("/checkCoreBankingAccount")
    ResponseEntity<CheckAccountResponse> checkCoreBankingAccount(@RequestBody CardRequest cardRequest){
        CheckAccountResponse checkAccountResponse = cardsService.checkCoreBankingAccount(cardRequest.getCardNumber(),cardRequest.getCvv(),cardRequest.getAmount());
        return ResponseEntity.ok(checkAccountResponse);
    }
}
