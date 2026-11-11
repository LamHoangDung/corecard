package com.example.corecard.service;

import lombok.Setter;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Locale;

@Service
public class GenerateService {
    private final SecureRandom random = new SecureRandom();

    public String generatePan(long sequenceNumber) {
        String body = "40000000" + String.format(Locale.ROOT, "%07d", sequenceNumber);
        int sum = 0;
        boolean doubleDigit = true;
        for (int i = body.length() - 1; i >= 0; i--) {
            int digit = body.charAt(i) - '0';
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }
        int checkDigit = (10 - sum % 10) % 10;
        return body + checkDigit;
    }
    public String generateCvv() {
        return String.format(Locale.ROOT, "%03d", random.nextInt(1000));
    }
}
