package com.jtt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@MapperScan(basePackages = "com.jtt.app.dao")
@Controller
public class ImServerApplication {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public static void main(String[] args) {
        SpringApplication.run(ImServerApplication.class, args);
    }

    @GetMapping("/index1")
    public String index1() {
        return "index.html";
    }
    @GetMapping("/index2")
    public String index2() {
        return "index1.html";
    }

    @Scheduled(fixedRate = 1000)
    @SendTo("/topic/test")
    public Object callback() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpMessagingTemplate.convertAndSend("/topic/test", dateFormat.format(new Date()));
        return "test";
    }

}
