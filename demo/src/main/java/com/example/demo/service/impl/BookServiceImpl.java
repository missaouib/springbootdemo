package com.example.demo.service.impl;

import com.example.demo.dao.BookDao;
import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = {"bookCache"})
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    /**
     * @Cacheable标注的方法，Spring在每次执行前都会检查Cache中是否存在相同key的缓存元素，
     * 如果存在就不再执行该方法，而是直接从缓存中获取结果进行返回，否则才会执行并将返回结果存入指定的缓存中
     * Spring-Cache默认put时是不加锁的，所以没有办法解决这个问题。但是可以设置 sync = true
     * @return
     */
    @Cacheable("bookList")// 标志读取缓存操作，如果缓存不存在，则调用目标方法，并将结果放入缓存
    @Override
    public List<Book> selectAll() {
        System.out.println("from db");
        return bookDao.selectAll();
    }

    @Cacheable(cacheNames = {"book"},key = "#id")
    @Override
    public Book findById(int id) {
        System.out.println("from db");
        return bookDao.findById(id);
    }

    /**
     * @CachePut 标注的方法在执行前不会去检查缓存中是否存在之前执行过的结果，
     * 而是每次都会执行该方法，并将执行结果以键值对的形式存入指定的缓存中
     * @param entity
     * @return
     */
    @CachePut(cacheNames = { "book" }, key = "#entity.id")
    @Override
    public Book insert(Book entity) {
        bookDao.insert(entity);
        return entity;
    }


    @CachePut(cacheNames = { "book" }, key = "#entity.id")//根据key清除缓存，一般该注解标注在修改和删除方法上
    @Override
    public Book update(Book entity) {
        bookDao.update(entity);
        System.out.println("from db");
        return entity;
    }

    /**
     * allEntries：非必需，默认为false。当为true时，会移除所有数据。如：@CachEvict(value=”testcache”,allEntries=true)
     * beforeInvocation：非必需，默认为false，会在调用方法之后移除数据。当为true时，会在调用方法之前移除数据
     *
     * @param id
     * @return
     */
    @CacheEvict(cacheNames = {"book"}, key = "#id", allEntries = false, beforeInvocation = true)
//根据key清除缓存，一般该注解标注在修改和删除方法上
    @Override
    public int delete(int id) {
        System.out.println("from db");
        return bookDao.delete(id);
    }

    @Override
    public int batchInsert(List<Book> list) {
        return bookDao.batchInsert(list);
    }

    @Override
    public int batchUpdateByPrimaryKeySelective(List<Book> list) {
        return bookDao.batchUpdateByPrimaryKeySelective(list);
    }
}
