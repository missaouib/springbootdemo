package com.example.demo.controler;

import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/html")
@Slf4j
@Validated
public class HTMLController {

    @Autowired
    private BookService bookService;

    @GetMapping("/getBook")
    public String getBook(Model model) {
        int id = 1;
        Book book = bookService.findById(id);
        //将用户信息保存到Model对象中
        model.addAttribute("book", book);
        return "book";
    }


}
