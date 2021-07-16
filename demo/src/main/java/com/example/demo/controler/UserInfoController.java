package com.example.demo.controler;

import com.example.demo.entity.UserInfo;
import com.example.demo.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController("/userinfo")
@RequestMapping
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RedissonClient redisson;

    @GetMapping("/{id}")
    public Object getUserInfo(@PathVariable Integer id) {
        UserInfo userInfo = userInfoService.getById(id);
        if (userInfo == null) {
            return "没有该用户";
        }
        return userInfo;
    }

    @GetMapping("/AutoLoad/{id}")
    public Object getUserInfoAutoLoad(@PathVariable Integer id) {
        UserInfo userInfo = userInfoService.getByIdAutoLoad(id);
        if (userInfo == null) {
            return "没有该用户";
        }
        return userInfo;
    }

    @PostMapping()
    public Object createUserInfo(@RequestBody UserInfo userInfo) {
        userInfoService.addUserInfo(userInfo);
        return "SUCCESS";
    }

    @PutMapping()
    public Object updateUserInfo(@RequestBody UserInfo userInfo) {
        UserInfo newUserInfo = userInfoService.updateUserInfo(userInfo);
        if (newUserInfo == null) {
            return "不存在该用户";
        }
        return newUserInfo;
    }

    @DeleteMapping("/{id}")
    public Object deleteUserInfo(@PathVariable Integer id) {
        userInfoService.deleteById(id);
        return "SUCCESS";
    }

    @GetMapping("/condition")
    public List<UserInfo> listByCondition(){
        UserInfo userInfo = new UserInfo();
        userInfo.setAge(10);
        List<UserInfo> userInfoList = userInfoService.listByCondition(userInfo);

        System.out.println(userInfoList);
        System.out.println(CollectionUtils.isEmpty(userInfoList));
        log.info("userinfo list {}",userInfoList);

        RBucket<List<UserInfo>> bucket = redisson.getBucket("key");
        bucket.set(userInfoList);
        List<UserInfo> list = bucket.get();
        System.out.println(list);
        System.out.println(CollectionUtils.isEmpty(list));
        log.info("userinfo list {}",list);
        return userInfoList;
    }
}
