package com.example.demo.controler;

import com.example.demo.VO.BookVO;
import com.example.demo.aspect.WebLog;
import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import com.example.demo.util.BeanConvertUtils;
import com.example.demo.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/book")
@Slf4j
@Validated
public class BookControler {
    @Autowired
    private BookService bookService;

    @PostMapping("/insert")
    public ResponseEntity insert(@Valid @RequestBody BookVO bookVO) {
        Book book = BeanConvertUtils.convert(bookVO, Book.class);
        bookService.insert(book);
        return ResponseUtils.ok(book);
    }

    @WebLog(description = "select")
    @GetMapping("/select/{id}")
    public ResponseEntity select(@PathVariable Integer id) {
        Book book = bookService.findById(id);
        return ResponseUtils.ok(book);
    }

    @GetMapping("/selectall")
    public List<Book> selectall() {
        return bookService.selectAll();
    }

    @GetMapping("/delete/{id}")
    public int delete(@PathVariable Integer id) {
        return bookService.delete(id);
    }


    @GetMapping("/update/{id}")
    public Book update(@PathVariable Integer id) {
        Book book = bookService.findById(id);
        book.setPrice(new BigDecimal(4534));
        return bookService.update(book);
    }

}
