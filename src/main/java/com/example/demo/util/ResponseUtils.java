package com.example.demo.util;

import com.example.demo.constants.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtils {

    public static <T> ResponseEntity<Map<String, Object>> ok(T data) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", ResponseCode.SUCCESS.getCode());
        body.put("data", data);
        body.put("message", "success");
        return new ResponseEntity(body, HttpStatus.OK);
    }

    public static <T> ResponseEntity<Map<String, Object>> error(int errorCode, T data, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", errorCode);
        body.put("data", data);
        body.put("message", message);
        return new ResponseEntity(body, HttpStatus.OK);
    }

    public static <T> ResponseEntity<Map<String, Object>> error(int errorCode, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", errorCode);
        body.put("data", "");
        body.put("message", message);
        return new ResponseEntity(body, HttpStatus.OK);
    }

    public static <T> ResponseEntity<Map<String, Object>> error(ResponseCode responseCode) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", responseCode.getCode());
        body.put("data", "");
        body.put("message", responseCode.getDesc());
        return new ResponseEntity(body, HttpStatus.OK);
    }

    public static <T> ResponseEntity<Map<String, Object>> paramError(String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", ResponseCode.PARAM_ERROR.getCode());
        body.put("data", "");
        body.put("message", message);
        return new ResponseEntity(body, HttpStatus.OK);
    }

    public static <T> ResponseEntity<Map<String, Object>> serverError(String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", ResponseCode.SERVER_ERROR.getCode());
        body.put("data", "");
        body.put("message", message);
        return new ResponseEntity(body, HttpStatus.OK);
    }
}