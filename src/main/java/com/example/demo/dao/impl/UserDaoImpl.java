package com.example.demo.dao.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import com.example.demo.entity.UserExample;
import com.example.demo.mapper.UserMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selectAll() {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        return userMapper.selectByExample(example);
    }

    @Override
    public User findById(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insert(User entity) {
        return userMapper.insert(entity);
    }

    @Override
    public int batchInsert(List<User> list) {
        return userMapper.batchInsert(list);
    }


    /**
     * 下面4个两两一组，因为第一种方式对代码顺序有要求，必须要在一起
     * 第二种方式使用lambda表达式，减少错误几率
     *
     */

    @Override
    public List<User> findAllByPageList1(int pageNum, int pageSize) {
        //想保证正确性必须要查询代码紧跟分页代码才行
        PageHelper.startPage(pageNum,pageSize);
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        return userMapper.selectByExample(example);
    }

    @Override
    public List<User> findAllByPageList2(int pageNum, int pageSize) {
        Page<User> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> {
            UserExample example = new UserExample();
            UserExample.Criteria criteria = example.createCriteria();
            userMapper.selectByExample(example);
        });
        //之所以可以返回List，是因为Page继承了ArrayList
        return page;
    }



    @Override
    public PageInfo<User> findAllByPagePageInfo1(int pageNum, int pageSize) {
        //想保证正确性必须要查询代码紧跟分页代码才行
        PageHelper.startPage(pageNum,pageSize);
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        List<User> users = userMapper.selectByExample(example);
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        return userPageInfo;
    }

    @Override
    public PageInfo<User> findAllByPagePageInfo2(int pageNum, int pageSize) {
        PageInfo<User> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            UserExample example = new UserExample();
            UserExample.Criteria criteria = example.createCriteria();
            userMapper.selectByExample(example);
        });
        return pageInfo;
    }


}
