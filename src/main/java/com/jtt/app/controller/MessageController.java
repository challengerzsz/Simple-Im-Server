package com.jtt.app.controller;

import com.jtt.app.common.ServerResponse;
import com.jtt.app.dao.IMessageMapper;
import com.jtt.app.dao.IQunMapper;
import com.jtt.app.dao.IUserMapper;
import com.jtt.app.model.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : zengshuaizhi
 * @date : 2019-06-30 21:02
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    public static final String PRIVATE_CHAT_TOPIC = "/topic/private";

    public static final String QUN_CHAT_TOPIC = "/topic/public";

    public static final int PRIVATE_CHAT_MSG = 0;

    public static final int QUN_CHAT_MSG = 1;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private IMessageMapper messageMapper;

    @Autowired
    private IQunMapper qunMapper;

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/sendToUser")
    public ServerResponse<Long> sendToUser(@RequestParam Long uid, @RequestParam Long receiver, @RequestParam String msg) {
        if (StringUtils.isBlank(msg)) {
            return ServerResponse.createBySuccessMsg("发送消息体不能为空");
        }

        Long timestamp = System.currentTimeMillis();
        Message message = new Message(uid, receiver, PRIVATE_CHAT_MSG, msg, timestamp);
        int result = messageMapper.insertNewMsg(message);
        if (result < 0) {
            return ServerResponse.createByErrorMsg("新消息持久化DB失败!");
        }

        simpMessagingTemplate.convertAndSendToUser(String.valueOf(receiver), PRIVATE_CHAT_TOPIC, message);

        return ServerResponse.createBySuccess("发送消息成功", timestamp);
    }

    @PostMapping("/sendToQun")
    public ServerResponse<Long> sendToQun(@RequestParam Long uid, @RequestParam String msg, @RequestParam Long qunId) {
        if (StringUtils.isBlank(msg)) {
            return ServerResponse.createBySuccessMsg("发送消息体不能为空!");
        }
        Long timestamp = System.currentTimeMillis();
        Message message = new Message(uid, qunId, QUN_CHAT_MSG, msg, timestamp);
        int result = messageMapper.insertNewMsg(message);
        if (result < 0) {
            return ServerResponse.createByErrorMsg("新消息持久化DB失败");
        }

        List<Long> qunMemberList = qunMapper.getQunMembers(qunId);
        for (Long uidTemp : qunMemberList) {
            if (uidTemp == uid) {
                continue;
            }
            simpMessagingTemplate.convertAndSendToUser(String.valueOf(uidTemp), QUN_CHAT_TOPIC, message);
        }

        return ServerResponse.createBySuccess("发送消息成功", timestamp);
    }

    @GetMapping("/getAllUnreadMsg/{uid}/{timestamp}")
    public ServerResponse<List<Message>> getAllUnreadMsg(@PathVariable Long uid, @PathVariable Long timestamp) {
        if (timestamp == null) {
            return ServerResponse.createByErrorMsg("时间戳不能为空");
        }

        List<Message> allUnreadMsgs = new ArrayList<>();
        List<Message> allUnreadPriMsgs = messageMapper.getAllUnreadPriMsgs(uid, timestamp, PRIVATE_CHAT_MSG);
        List<Message> allUnreadQunMsgs = messageMapper.getAllUnreadQunMsgs(uid, timestamp, QUN_CHAT_MSG);
        if (!CollectionUtils.isEmpty(allUnreadPriMsgs)) {
            allUnreadMsgs.addAll(allUnreadPriMsgs);
        }
        if (!CollectionUtils.isEmpty(allUnreadQunMsgs)) {
            allUnreadMsgs.addAll(allUnreadQunMsgs);
        }

        return ServerResponse.createBySuccess("查询成功", allUnreadMsgs);
    }

}
