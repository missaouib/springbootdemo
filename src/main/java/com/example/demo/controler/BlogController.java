package com.example.demo.controler;

import com.example.demo.entity.Blog;
import com.example.demo.service.BlogServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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
        blog.setCreatetime(new Date());
        blog.setUpdatetime(new Date());
        blogServer.insert(blog);
        return blog.toString();
    }

    @GetMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        blogServer.delete(id);
    }

    @GetMapping("update/{id}")
    public void update(@PathVariable int id){
        Blog blog = new Blog();
        blog.setId(id);
        blog.setAuthor("author");
        blog.setContent("context");
        blog.setSubtitle("subtitle");
        blog.setTitle("title");
        blog.setCreatetime(new Date());
        blog.setUpdatetime(new Date());
        blogServer.update(blog);
    }

    @GetMapping("find/{id}")
    public String find(@PathVariable int id){
        Blog blog = blogServer.findById(id);
        return blog.toString();
    }


}
