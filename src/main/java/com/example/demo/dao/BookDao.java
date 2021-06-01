package com.example.demo.dao;

import com.example.demo.entity.Book;

import java.util.List;

public interface BookDao {
    List<Book> selectAll();

    Book findById(int id);

    int insert(Book entity);

    int update(Book entity);

    int delete(int id);

    int batchInsert(List<Book> list);

    int batchUpdateByPrimaryKeySelective(List<Book> list);
}
