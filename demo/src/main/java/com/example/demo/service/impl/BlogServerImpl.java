package com.example.demo.service.impl;

import com.example.demo.cache.RedisService;
import com.example.demo.entity.Blog;
import com.example.demo.entity.BlogExample;
import com.example.demo.mapper.secondary.BlogMapper;
import com.example.demo.service.BlogServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BlogServerImpl implements BlogServer {
    @Autowired(required = false)
    private BlogMapper blogMapper;
    @Autowired
    private RedisService redisService;
    @Override
    public List<Blog> selectAll() {
        BlogExample example = new BlogExample();
        return blogMapper.selectByExample(example);
    }

    @Override
    public Blog findById(int id) {
        Blog blog = (Blog) redisService.get("blog:" + id);
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
    public List<Blog> findByCondition(Blog entity) {
        BlogExample example = new BlogExample();
        example.createCriteria().andAuthorEqualTo(entity.getAuthor()).andSubtitleEqualTo(entity.getSubtitle()).andTitleEqualTo(entity.getTitle());
        List<Blog> blogs = blogMapper.selectByExample(example);
        return blogs;
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
