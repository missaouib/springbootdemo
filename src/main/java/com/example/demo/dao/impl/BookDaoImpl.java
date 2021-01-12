package com.example.demo.dao.impl;

import com.example.demo.dao.BookDao;
import com.example.demo.entity.Book;
import com.example.demo.entity.BookExample;
import com.example.demo.mapper.primary.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookDaoImpl implements BookDao {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public List<Book> selectAll() {
        BookExample example = new BookExample();
        BookExample.Criteria criteria = example.createCriteria();
        return bookMapper.selectByExample(example);
    }

    @Override
    public Book findById(int id) {
        return bookMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insert(Book entity) {
        return bookMapper.insert(entity);
    }

    @Override
    public int update(Book entity) {
        return bookMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int delete(int id) {
        return bookMapper.deleteByPrimaryKey(id);
    }
}
