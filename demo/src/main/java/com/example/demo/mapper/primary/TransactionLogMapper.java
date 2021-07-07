package com.example.demo.mapper.primary;

import com.example.demo.entity.TransactionLog;
import com.example.demo.entity.TransactionLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TransactionLogMapper {
    long countByExample(TransactionLogExample example);

    int deleteByExample(TransactionLogExample example);

    int deleteByPrimaryKey(String id);

    int insert(TransactionLog record);

    int insertSelective(TransactionLog record);

    List<TransactionLog> selectByExample(TransactionLogExample example);

    TransactionLog selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TransactionLog record, @Param("example") TransactionLogExample example);

    int updateByExample(@Param("record") TransactionLog record, @Param("example") TransactionLogExample example);

    int updateByPrimaryKeySelective(TransactionLog record);

    int updateByPrimaryKey(TransactionLog record);
}