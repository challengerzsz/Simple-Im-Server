package com.jtt.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : zengshuaizhi
 * @date : 2019-06-29 22:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private Long sender;
    private User senderInfo;
    private Long receiver;
    private String msg;
    /**
     * 0 私聊 1 群聊
     */
    private Integer type;

    public Message(Long sender, Long receiver, String msg, Integer type) {
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
        this.type = type;
    }
}
