package com.example.demo.mapper;

import com.example.demo.entity.Points;
import com.example.demo.entity.PointsExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PointsMapper {
    long countByExample(PointsExample example);

    int deleteByExample(PointsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Points record);

    int insertSelective(Points record);

    List<Points> selectByExample(PointsExample example);

    Points selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Points record, @Param("example") PointsExample example);

    int updateByExample(@Param("record") Points record, @Param("example") PointsExample example);

    int updateByPrimaryKeySelective(Points record);

    int updateByPrimaryKey(Points record);
}