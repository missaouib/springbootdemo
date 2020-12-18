package com.example.demo.controler;

import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController()
@RequestMapping("/book")
@Slf4j
public class BookControler {
    @Autowired
    private BookService bookService;

    @GetMapping("/insert/{id}")
    private Book insert(@PathVariable Integer id){
        Book book = new Book();
        book.setId(id);
        book.setName("飘");
        book.setPrice(new BigDecimal(45));
        return bookService.insert(book);
    }

    @GetMapping("/select/{id}")
    private Book select(@PathVariable Integer id){
        return bookService.findById(id);
    }

    @GetMapping("/selectall")
    private List<Book> selectall(){
        return bookService.selectAll();
    }

    @GetMapping("/delete/{id}")
    private int delete(@PathVariable Integer id){
        return bookService.delete(id);
    }


    @GetMapping("/update/{id}")
    private Book update(@PathVariable Integer id){
        Book book = bookService.findById(id);
        book.setPrice(new BigDecimal(4534));
        return bookService.update(book);
    }

}
