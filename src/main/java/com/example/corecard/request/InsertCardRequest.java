package com.example.corecard.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertCardRequest {
    private String fullName;
    private String cardProductType;
    private String phone;
}
