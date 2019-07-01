package com.jtt.app.controller;

import com.jtt.app.common.ServerResponse;
import com.jtt.app.dao.IMessageMapper;
import com.jtt.app.dao.IQunMapper;
import com.jtt.app.dao.IUserMapper;
import com.jtt.app.model.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private IUserMapper userMapper;

    @PostMapping("/sendToUser")
    public ServerResponse<String> sendToUser(@RequestParam Long uid, @RequestParam Long receiver, @RequestParam String msg) {
        if (StringUtils.isBlank(msg)) {
            return ServerResponse.createBySuccessMsg("发送消息体不能为空");
        }

        int result = messageMapper.insertNewPrivateMsg(uid, receiver, msg);
        if (result < 0) {
            return ServerResponse.createByErrorMsg("新消息持久化DB失败");
        }

        Message message = new Message(uid, receiver, msg, PRIVATE_CHAT_MSG);
        message.setSenderInfo(userMapper.getUserInfo(uid));
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(receiver), PRIVATE_CHAT_TOPIC, message);

        return ServerResponse.createBySuccessMsg("发送消息成功");
    }

    @PostMapping("/sendToQun")
    public ServerResponse<String> sendToQun(@RequestParam Long uid, @RequestParam String msg, @RequestParam Long qunId) {
        if (StringUtils.isBlank(msg)) {
            return ServerResponse.createBySuccessMsg("发送消息体不能为空");
        }

        int result = messageMapper.insertNewQunMsg(uid, qunId, msg);
        if (result < 0) {
            return ServerResponse.createByErrorMsg("新消息持久化DB失败");
        }

        Message message = new Message(uid, qunId, msg, QUN_CHAT_MSG);

        List<Long> qunMemberList = qunMapper.getQunMembers(qunId);
        for (Long uidTemp : qunMemberList) {
            if (uidTemp == uid) {
                continue;
            }
            message.setSenderInfo(userMapper.getUserInfo(uid));
            simpMessagingTemplate.convertAndSendToUser(String.valueOf(uidTemp), QUN_CHAT_TOPIC, message);
        }

        return ServerResponse.createBySuccessMsg("发送消息成功");
    }

}
