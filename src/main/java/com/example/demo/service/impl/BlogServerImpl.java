package com.example.demo.service.impl;

import com.example.demo.entity.Blog;
import com.example.demo.entity.BlogExample;
import com.example.demo.mapper.secondary.BlogMapper;
import com.example.demo.service.BlogServer;
import com.example.demo.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BlogServerImpl implements BlogServer {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public List<Blog> selectAll() {
        BlogExample example = new BlogExample();
        return blogMapper.selectByExample(example);
    }

    @Override
    public Blog findById(int id) {
        Blog blog = (Blog) redisUtil.get("blog:" + id);
        if (blog == null) {
            System.out.println("from db");
            return blogMapper.selectByPrimaryKey(id);
        }
        else {
            System.out.println("from cache");
            return blog;
        }
    }

    @Override
    public int insert(Blog entity) {
        return blogMapper.insert(entity);
    }

    @Override
    public int update(Blog entity) {
        return blogMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int delete(int id) {
        
        return blogMapper.deleteByPrimaryKey(id);
    }
}
