package org.example.opp_cw.dto;

import lombok.Data;
import org.springframework.security.crypto.keygen.KeyGenerators;

import java.util.Arrays;

@Data
public class RandomSecureCode {
    private String code;

    public RandomSecureCode() {
        this.code = Arrays.toString(KeyGenerators.secureRandom().generateKey());
    }

    public RandomSecureCode(String code) {
        this.code = code;
    }
}
