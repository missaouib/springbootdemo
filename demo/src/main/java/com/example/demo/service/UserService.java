package com.example.demo.service;

import com.example.demo.entity.User;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.concurrent.Future;

public interface UserService {
    List<User> getList();

    int insert(User user);

    int batchInsert(List<User> list);

    User findById(int id);

    /**
     *
     * @param pageNum 代表当前页
     * @param pageSize 代表每页多少条数据
     * @return 返回所需对象的list集合
     */
    List<User> findAllByPageList1(int pageNum, int pageSize);

    List<User> findAllByPageList2(int pageNum, int pageSize);

    /**
     *
     * @param pageNum 代表当前页
     * @param pageSize 代表每页多少条数据
     * @return 返回PageInfo<T>对象，包括所需对象的list集合，还包括数据库总记录数，总页数，当前页，下一页等诸多信息
     */
    PageInfo<User> findAllByPagePageInfo1(int pageNum, int pageSize);

    PageInfo<User> findAllByPagePageInfo2(int pageNum, int pageSize);

    void multiThread();

    void multiThreadId(int id);

    Future<User> multiThreadFutureId(int id);

    Future<String> doTask1() throws Exception;

    Future<String> doTask2() throws Exception;

    Future<String> doTask3() throws Exception;

}
