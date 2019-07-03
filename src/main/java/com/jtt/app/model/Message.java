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

    private Long id;
    private Long sender;
    private Long receiver;
    private String msg;
    /**
     * 0 私聊 1 群聊
     */
    private Integer type;
    private Long createTime;

    public Message(Long sender, Long receiver, Integer type, String msg, Long createTime) {
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
        this.createTime = createTime;
        this.type = type;
    }
}
