package com.example.demo.controller;

import com.example.demo.VO.BookVO;
import com.example.demo.aspect.WebLog;
import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import com.example.demo.util.BeanConvertUtils;
import com.example.demo.util.ResponseUtils;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/book")
@Slf4j
@Validated
@Api(tags = "书籍管理")
public class BookControler {
    @Autowired
    private BookService bookService;

    @PostMapping("/insert")
    @ApiOperation("添加书籍")
    public ResponseEntity insert(@Valid @RequestBody BookVO bookVO) {
        Book book = BeanConvertUtils.convert(bookVO, Book.class);
        bookService.insert(book);
        return ResponseUtils.ok(book);
    }

    @WebLog(description = "select")
    @GetMapping("/select/{id}")
    @ApiOperation("按id查询数据")
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path", dataType = "int")
    public ResponseEntity select(@PathVariable Integer id) {
        Book book = bookService.findById(id);
        log.info("book: {}", book);
        return ResponseUtils.ok(book);
    }

    @ApiOperation("书籍列表")
    @GetMapping("/selectall")
    public List<Book> selectall() {
        return bookService.selectAll();
    }

    @DeleteMapping("/delete/{id}")
    public int delete(@PathVariable Integer id) {
        return bookService.delete(id);
    }


    @PutMapping("/update/{id}")
    public Book update(@PathVariable Integer id) {
        Book book = bookService.findById(id);
        book.setPrice(new BigDecimal(4534));
        return bookService.update(book);
    }

    @PostMapping("/batchInsert")
    public ResponseEntity batchInsert() {
        Book book1 = new Book();
        book1.setPrice(new BigDecimal("12.34"));
        book1.setName("book 1");
        Book book2 = new Book();
        book2.setPrice(new BigDecimal("11.34"));
        book2.setName("book 2");
        ArrayList<Book> books = Lists.newArrayList(book1, book2);
        bookService.batchInsert(books);
        return ResponseUtils.ok(books);
    }

    @PutMapping("batchUpdate")
    public ResponseEntity batchUpdate() {
        List<Book> list = new ArrayList<>();
        Book book1 = new Book();
        book1.setId(1);
        book1.setPrice(new BigDecimal(11));
        book1.setName("book1");

        Book book2 = new Book();
        book2.setId(2);
        book2.setPrice(new BigDecimal(22));
        list.add(book1);
        list.add(book2);
        int line = bookService.batchUpdateByPrimaryKeySelective(list);
        return ResponseUtils.ok(line);
    }
}
