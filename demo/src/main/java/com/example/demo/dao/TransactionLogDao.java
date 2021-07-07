package com.example.demo.dao;

import com.example.demo.entity.TransactionLog;

public interface TransactionLogDao {

    TransactionLog findById(String id);

    int insert(TransactionLog entity);

    int update(TransactionLog entity);

    int delete(String id);
}
