package com.example.demo.service.impl;

import com.example.demo.entity.Order;
import com.example.demo.entity.Points;
import com.example.demo.mapper.PointsMapper;
import com.example.demo.service.PointsService;
import com.example.demo.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PointsServiceImpl implements PointsService {

    @Autowired
    private PointsMapper pointsMapper;

    SnowFlake snowFlake = new SnowFlake(1,1);

    @Override
    public void increasePoints(Order order) {
        //入库之前先查询，实现幂等
        if (pointsMapper.selectByPrimaryKey(order.getOrderNo())!= null){
            log.info("积分添加完成，订单已处理。{}",order.getOrderNo());
        }else{
            Points points = new Points();
            points.setId(snowFlake.nextId());
            points.setUserId(order.getUserId());
            points.setOrderNo(order.getOrderNo());
            Integer amount = order.getAmount();
            points.setPoints(amount.intValue()*10);
            points.setRemarks("商品消费共【"+order.getAmount()+"】元，获得积分"+points.getPoints());
            pointsMapper.insert(points);
            log.info("已为订单号码{}增加积分。",points.getOrderNo());
        }
    }
}
