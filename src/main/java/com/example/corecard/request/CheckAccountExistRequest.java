package com.example.corecard.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CheckAccountExistRequest {
    private String phone;
    private String fullName;
}
