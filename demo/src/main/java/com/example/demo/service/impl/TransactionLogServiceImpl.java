package com.example.demo.service.impl;

import com.example.demo.dao.TransactionLogDao;
import com.example.demo.service.TransactionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionLogServiceImpl implements TransactionLogService {
    @Autowired
    private TransactionLogDao transactionLogDao;

    @Override
    public int get(String transactionId) {
        return transactionLogDao.findById(transactionId) == null ? -1 : 1;
    }
}
