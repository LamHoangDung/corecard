package com.example.corecard.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckAccountValidResponse {
    private String code;
    private boolean eligible;
    private String message;
}