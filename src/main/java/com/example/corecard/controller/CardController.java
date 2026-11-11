package com.example.corecard.controller;

import com.example.corecard.request.CardRequest;
import com.example.corecard.request.CheckAccountExistRequest;
import com.example.corecard.request.InsertCardRequest;
import com.example.corecard.response.BaseResponse;
import com.example.corecard.response.CheckAccountValidResponse;
import com.example.corecard.response.Corebank.CoreBankAccountResponse;
import com.example.corecard.response.InsertCardResponse;
import com.example.corecard.service.CardsService;
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
    ResponseEntity<CheckAccountValidResponse> checkCoreBankingAccount(@RequestBody CardRequest cardRequest){
        CheckAccountValidResponse checkAccountResponse = cardsService.checkCoreBankingAccount(cardRequest.getCardNumber(),cardRequest.getCvv(),cardRequest.getAmount());
        return ResponseEntity.ok(checkAccountResponse);
    }

    @PostMapping("checkAccountExistByNameAndPhone")
    ResponseEntity<CoreBankAccountResponse> checkAccountExistByNameAndPhone(@RequestBody InsertCardRequest request){
        return ResponseEntity.ok(cardsService.checkAccountExistByPhoneAndNumber(request));
    }

    @PostMapping("/insertCard")
    public ResponseEntity<InsertCardResponse> insertCard(@RequestBody InsertCardRequest request) {
        return ResponseEntity.ok(cardsService.insertCard(request));
    }
}
