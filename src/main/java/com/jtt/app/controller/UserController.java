package com.jtt.app.controller;

import com.jtt.app.common.ServerResponse;
import com.jtt.app.dao.IUserMapper;
import com.jtt.app.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author : zengshuaizhi
 * @date : 2019-06-30 15:18
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    public static final String UID_TOKEN_PREFIX = "UID-TOKEN-HASH";

    @Autowired
    private IUserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/login")
    public ServerResponse<User> login(@RequestParam Long uid, @RequestParam String password) {
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(password)) {
            return ServerResponse.createByErrorMsg("账号或密码为空，请重试");
        }

        User user = userMapper.login(uid, password);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            redisTemplate.opsForHash().put(UID_TOKEN_PREFIX, uid, token);
            user.setToken(token);
            return ServerResponse.createBySuccess("登录成功", user);
        }

        return ServerResponse.createByErrorMsg("账号不存在或密码错误");
    }

    @PostMapping("/register")
    public ServerResponse<User> register(@RequestParam String username, @RequestParam String password) {

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ServerResponse.createByErrorMsg("昵称和密码不能为空");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        int result = userMapper.register(user);
        if (result < 0) {
            return ServerResponse.createBySuccessMsg("注册失败");
        }


        return ServerResponse.createBySuccess("注册成功，请牢记uid", user);
    }

    @GetMapping("/getFriends/{uid}")
    public ServerResponse<List<User>> getFriends(@PathVariable Long uid) {
        List<User> friends = userMapper.getFriends(uid);
        if (CollectionUtils.isEmpty(friends)) {
            return ServerResponse.createByErrorMsg("抱歉，您现在还没有好友，快去添加吧");
        }

        return ServerResponse.createBySuccess("查询成功", friends);
    }

    @PostMapping("/addFriend")
    public ServerResponse<String> addFriends(@RequestParam Long uid, @RequestParam Long friendUid) {

        int hasBeenFriend = userMapper.checkIfIsFriend(uid, friendUid);
        if (hasBeenFriend == 1) {
            return ServerResponse.createByErrorMsg("您已添加其为好友");
        }

        // 双向好友关系，先这么写
        int result = userMapper.addFriends(uid, friendUid);
        int result2 = userMapper.addFriends(friendUid, uid);
        if (result <= 0 || result2 <= 0) {
            return ServerResponse.createByErrorMsg("添加好友失败");
        }

        return ServerResponse.createBySuccessMsg("添加好友成功");
    }

    @GetMapping("/getInfo/{uid}")
    public ServerResponse<User> getUserInfo(@PathVariable Long uid) {
        User user = userMapper.getUserInfo(uid);
        if (user == null) {
            return ServerResponse.createByErrorMsg("该用户不存在！");
        }

        return ServerResponse.createBySuccess("查询成功", user);
    }
}
