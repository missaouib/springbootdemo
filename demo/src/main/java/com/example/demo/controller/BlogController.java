package com.example.demo.controller;

import com.example.demo.cache.RedisService;
import com.example.demo.entity.Blog;
import com.example.demo.service.BlogServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private BlogServer blogServer;

    @GetMapping("/insert")
    public String insert() {
        Blog blog = new Blog();
        blog.setAuthor("author");
        blog.setContent("context");
        blog.setSubtitle("subtitle");
        blog.setTitle("title");
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blogServer.insert(blog);
        return blog.toString();
    }

    @GetMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        blogServer.delete(id);
    }

    @GetMapping("update/{id}")
    public void update(@PathVariable int id) {
        Blog blog = new Blog();
        blog.setId(id);
        blog.setAuthor("author");
        blog.setContent("context");
        blog.setSubtitle("subtitle");
        blog.setTitle("title:  " + new Random().nextInt(100));
        blog.setUpdateTime(new Date());
        blog.setCreateTime(new Date());
        blogServer.update(blog);
    }

    @GetMapping("find/{id}")
    public String find(@PathVariable int id) {
        Blog blog = (Blog) redisService.get("blog:" + id);
        if (blog == null) {
            blog = blogServer.findById(id);
        }
        redisService.set("blog:" + id, blog, 30 * 60, TimeUnit.SECONDS);
        return blog == null ? "null" : blog.toString();
    }

    @GetMapping("findall")
    public List<Blog> findAll() {
        List<Blog> blogList = blogServer.selectAll();
        return blogList;
    }

    @GetMapping("findByCondition")
    public String findByCondition() {
        Blog blog = new Blog();
        blog.setAuthor("author1");
        blog.setTitle("title1");
        blog.setSubtitle("subtitle");
        List<Blog> blogList = blogServer.findByCondition(blog);
        return blogList.toString();
    }
}
