package com.jtt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImServerApplicationTests {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Test
    public void sendMessage() {
        simpMessagingTemplate.convertAndSend("/topic/callback", "hello!");
    }

}
