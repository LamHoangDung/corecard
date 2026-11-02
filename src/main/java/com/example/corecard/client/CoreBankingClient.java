package com.example.corecard.client;


import com.example.corecard.request.CheckAccountRequest;
import com.example.corecard.response.BaseResponse;
import com.example.corecard.response.CheckAccountResponse;
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

    public CheckAccountResponse checkAccount(CheckAccountRequest request) {
        CheckAccountResponse response = restClient.post()
                .uri("/account/check")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(CheckAccountResponse.class);
        return response;
    }


}
