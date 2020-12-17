package com.example.demo.dao;

import com.example.demo.entity.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserDao {
    List<User> selectAll();

    User findById(int id);

    int insert(User entity);

    int batchInsert(List<User> list);

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

}
