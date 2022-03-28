package com.simple.mall;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static boolean isSigned(HttpSession session){
        return !(session == null || session.getAttribute("memberNo") == null);
    }

    public ResponseEntity<?> success(ResultMessage resultMessage){
        return new ResponseEntity<>(resultMessage.toMap(), HttpStatus.OK);
    }

    public ResponseEntity<?> success(String code, String message){
        Map<String, String> result = new HashMap<>();
        result.put("code", code);
        result.put("message", message);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<?> error(String code, String message){
        Map<String, String> result = new HashMap<>();
        result.put("code", code);
        result.put("message", message);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
