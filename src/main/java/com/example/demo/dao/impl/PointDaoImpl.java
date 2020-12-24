package com.example.demo.dao.impl;

import com.example.demo.dao.PointDao;
import com.example.demo.entity.Points;
import com.example.demo.mapper.PointsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PointDaoImpl implements PointDao {
    @Autowired
    private PointsMapper pointsMapper;

    @Override
    public Points findById(Long id) {
        return pointsMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insert(Points entity) {
        return pointsMapper.insert(entity);
    }

    @Override
    public int update(Points entity) {
        return pointsMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int delete(Long id) {
        return pointsMapper.deleteByPrimaryKey(id);
    }
}
