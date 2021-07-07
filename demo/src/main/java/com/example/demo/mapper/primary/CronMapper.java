package com.example.demo.mapper.primary;

import com.example.demo.entity.Cron;
import com.example.demo.entity.CronExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CronMapper {
    long countByExample(CronExample example);

    int deleteByExample(CronExample example);

    int deleteByPrimaryKey(String cronId);

    int insert(Cron record);

    int insertSelective(Cron record);

    List<Cron> selectByExample(CronExample example);

    Cron selectByPrimaryKey(String cronId);

    int updateByExampleSelective(@Param("record") Cron record, @Param("example") CronExample example);

    int updateByExample(@Param("record") Cron record, @Param("example") CronExample example);

    int updateByPrimaryKeySelective(Cron record);

    int updateByPrimaryKey(Cron record);
}