package com.example.demo.dao.impl;

import com.example.demo.entity.TransactionLog;
import com.example.demo.dao.TransactionLogDao;
import com.example.demo.mapper.primary.TransactionLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionLogDaoImpl implements TransactionLogDao {

    @Autowired
    private TransactionLogMapper transactionLogMapper;

    @Override
    public TransactionLog findById(String id) {
        return transactionLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insert(TransactionLog entity) {
        return transactionLogMapper.insert(entity);
    }

    @Override
    public int update(TransactionLog entity) {
        return transactionLogMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int delete(String id) {
        return transactionLogMapper.deleteByPrimaryKey(id);
    }
}
