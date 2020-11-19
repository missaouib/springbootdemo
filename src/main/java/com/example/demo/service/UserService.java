package com.example.demo.service;

import com.example.demo.entity.User;

import java.util.List;
import java.util.concurrent.Future;

public interface UserService {
    List<User> getList();

    int insert(User user);

    int batchInsert(List<User> list);

    User findById(int id);

    void multiThread();

    void multiThreadId(int id);

    Future<User> multiThreadFutureId(int id);

    public Future<String> doTask1() throws Exception;

    public Future<String> doTask2() throws Exception;

    public Future<String> doTask3() throws Exception;

}
