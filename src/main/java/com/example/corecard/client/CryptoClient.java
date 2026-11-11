package com.example.corecard.client;

import com.example.corecard.request.CryptoRequest;
import com.example.corecard.response.CryptoResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CryptoClient {
    private RestClient restClient;

    @Value("${crypto.base-url}")
    private String baseUrl;

    @PostConstruct
    public void init() {
        restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public String hashing (CryptoRequest cryptoRequest){
        CryptoResponse response = restClient.post()
                .uri("crypto/hash")
                .contentType(MediaType.APPLICATION_JSON)
                .body(cryptoRequest)
                .retrieve()
                .body(CryptoResponse.class);
        return response.getResult();
    }

    public String encrypt(CryptoRequest request) {
        CryptoResponse response = restClient.post()
                .uri("/crypto/encrypt")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(CryptoResponse.class);
        return response.getResult();
    }

}
