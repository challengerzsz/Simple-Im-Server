package com.jtt.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : zengshuaizhi
 * @date : 2019-06-29 22:09
 */
@Data
@AllArgsConstructor
public class Message {

    private Long sender;
    private Long receiver;
    private String msg;
}
