package com.example.demo.controler;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {

    /**
     * 使用基于 constructor 注入，而不是基于 field 注入
     */
    private final UserService userService;

    /**
     * 如果一个bean只有一个构造器，就可以省略@Autowired
     *
     * @param userService
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public List<User> getUserList() {
        return userService.getList();
    }

    @GetMapping("/findbyid")
    public void findById() {
        User user = userService.findById(2);
        System.out.println(user);
    }

    @GetMapping("insert")
    public void insert() throws ParseException {
        User user = new User();
        user.setUsername("hi");
        user.setPassword("passwd");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        user.setCreateTime(simpleDateFormat.parse("2011-1-1 1:1:11"));
//        user.setUpdateTime(simpleDateFormat.parse("2011-1-11 1:1:11"));
        int res = userService.insert(user);
        log.error("res: {}", res);
        log.error("主键: {}", user.getId());
    }

    /**
     * 测试mybatis批量插入
     */
    @GetMapping("batchInsert")
    public void batch() throws ParseException {
        long start = System.currentTimeMillis();
        ArrayList<User> list = new ArrayList<>();
        int size = 30;
        for (int i = 1; i <= size; i++) {
            Random r = new Random();
            int randNum = Math.abs(r.nextInt() % 10000);
            User user = new User();
            user.setPassword("000" + i + randNum);
            user.setUsername("wang" + i + randNum);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            user.setCreateTime(simpleDateFormat.parse("2011-1-1 1:1:11"));
            user.setUpdateTime(simpleDateFormat.parse("2011-1-11 1:1:11"));
            list.add(user);
        }
        int res = userService.batchInsert(list);
        log.error("res: " + res);
        list.stream().map(User::getId).forEach(System.out::println);
        long end = System.currentTimeMillis();
        log.error("time: " + (end - start));
    }

    @GetMapping("/pageList1/{pageNum}/{pageSize}")
    public Object pageList1(@PathVariable int pageNum, @PathVariable int pageSize) {
        List<User> list = userService.findAllByPageList1(pageNum, pageSize);
        System.out.println(list);
        return list;
//        System.err.println(userService.findAllByPageList1(pageNum, pageSize));
    }

    @GetMapping("/pagePageInfo1/{pageNum}/{pageSize}")
    public Object pagePageInfo1(@PathVariable int pageNum, @PathVariable int pageSize) {
        PageInfo<User> pageInfo = userService.findAllByPagePageInfo1(pageNum, pageSize);
        System.out.println(pageInfo);
        return pageInfo;
//        System.err.println(userService.findAllByPageList1(pageNum, pageSize));
    }

    @GetMapping("/pageList2/{pageNum}/{pageSize}")
    public Object pageList2(@PathVariable int pageNum, @PathVariable int pageSize) {
        List<User> list = userService.findAllByPageList2(pageNum, pageSize);
        System.out.println(list);
        return list;
//        System.err.println(userService.findAllByPageList1(pageNum, pageSize));
    }

    @GetMapping("/pagePageInfo2/{pageNum}/{pageSize}")
    public Object pagePageInfo2(@PathVariable int pageNum, @PathVariable int pageSize) {
        PageInfo<User> pageInfo = userService.findAllByPagePageInfo2(pageNum, pageSize);
        System.out.println(pageInfo);
        return pageInfo;
//        System.err.println(userService.findAllByPageList1(pageNum, pageSize));
    }


    /**
     * 测试异步 无返回值 异常
     */
    @GetMapping("/mul")
    public void mulTest() {
        userService.multiThreadId(1);
    }

    /**
     * 测试带有timeout的future
     */
    @GetMapping("/mulFuture")
    public void mulFuture() {
        Future<User> future = userService.multiThreadFutureId(2);
        try {
            User user = future.get(7, TimeUnit.SECONDS);
            log.error(user.toString());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多个异步线程全部结束才继续执行
     *
     * @throws Exception
     */
    @GetMapping("/mulTask")
    public void mulTask() throws Exception {
        long start = System.currentTimeMillis();
        Future<String> task1 = userService.doTask1();
        Future<String> task2 = userService.doTask2();
        Future<String> task3 = userService.doTask3();
        while (true) {
            if (task1.isDone() && task2.isDone() && task3.isDone()) {
                log.error("all done");
                break;
            }
        }
        long end = System.currentTimeMillis();
        log.error("3个任务总耗时: " + (end - start));
    }

    @GetMapping("random")
    @Transactional(rollbackFor = Exception.class)
    public void random() {
        long start = System.currentTimeMillis();
        int size = 1000;
        for (int i = 1; i <= size; i++) {
            Random r = new Random();
            int randNum = Math.abs(r.nextInt() % 10000);
            User user = new User();
            user.setPassword("000" + i + randNum);
            user.setUsername("wang" + i + randNum);
            userService.insert(user);
//            if(i== 20){
//                int a = 1/0;
//            }
        }
        long end = System.currentTimeMillis();
        log.error("time: " + (end - start));
    }
}