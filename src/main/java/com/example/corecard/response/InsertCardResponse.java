package com.example.corecard.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class InsertCardResponse {
    private String message;
    private String code;
}
