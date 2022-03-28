package com.simple.mall;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class ResponseMall {

    String code;
    String message;

    public ResponseMall(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
