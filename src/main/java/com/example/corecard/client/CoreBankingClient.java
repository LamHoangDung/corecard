package com.example.corecard.client;


import com.example.corecard.request.CheckAccountExistRequest;
import com.example.corecard.request.CheckAccountValidRequest;
import com.example.corecard.request.InsertCardRequest;
import com.example.corecard.response.AccountInfoResponse;
import com.example.corecard.response.BaseResponse;
import com.example.corecard.response.CheckAccountValidResponse;
import com.example.corecard.response.Corebank.CoreBankAccountResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CoreBankingClient {

    private RestClient restClient;

    @Value("${corebank.base-url}")
    private String baseUrl;

    @PostConstruct
    public void init() {
        restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public CheckAccountValidResponse checkAccountValid(CheckAccountValidRequest request) {
        CheckAccountValidResponse response = restClient.post()
                .uri("/account/checkValid")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(CheckAccountValidResponse.class);
        return response;
    }


    public CoreBankAccountResponse checkAccountExistByNameAndPhone(InsertCardRequest request){
         CoreBankAccountResponse response = restClient.post()
                .uri("bank/account/checkExistByNameAndPhone")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(CoreBankAccountResponse.class);
        return response;
    }




}
