package com.example.demo.exception;

import com.example.demo.util.ResponseUtils;
import constants.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        log.warn("【统一异常处理】异常", e);
        Map<String, Object> body = new HashMap<>();
        body.put("code", ResponseCode.SERVER_ERROR.getCode());
        body.put("data", null);
        body.put("message", ResponseCode.SERVER_ERROR.getDesc() + ", " + e);
        return ResponseUtils.serverError("");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        StringBuilder sbLog = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getDefaultMessage()).append(", ");
            sbLog.append(fieldError.getField()).append(", ");
        }

        sb = sb.deleteCharAt(sb.length() - 2);
        sbLog = sbLog.deleteCharAt(sbLog.length() - 2);
        log.warn("{}", sbLog.append("is null or invalid"));
        String msg = sb.toString().trim();
        return ResponseUtils.paramError(msg);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity handleMethodArgumentNotValidException(ConstraintViolationException e) {
        StringBuilder sb = new StringBuilder();
        String[] msgs = e.getMessage().split(", ");
        for (String msg : msgs) {
            String[] fieldAndMsg = msg.split(": ");
            String message = fieldAndMsg[1];
            sb.append(message).append(", ");
        }
        sb.deleteCharAt(sb.length() - 2);
        log.warn("{}", sb);
        return ResponseUtils.paramError(sb.toString());
    }

}
