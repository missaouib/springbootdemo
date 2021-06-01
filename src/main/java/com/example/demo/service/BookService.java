package com.example.demo.service;

import com.example.demo.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> selectAll();

    Book findById(int id);

    Book insert(Book entity);

    Book update(Book entity);

    int delete(int id);

    int batchInsert(List<Book> list);

    int batchUpdateByPrimaryKeySelective(List<Book> list);
}
