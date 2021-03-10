package com.example.demo.controler;

import com.example.demo.entity.Blog;
import com.example.demo.mapper.secondary.BlogMapper;
import com.example.demo.service.BlogServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@Slf4j
@RequestMapping("/blog")
public class BlogController {
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
        Blog blog = blogServer.findById(id);
        return blog == null ? "null" : blog.toString();
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
