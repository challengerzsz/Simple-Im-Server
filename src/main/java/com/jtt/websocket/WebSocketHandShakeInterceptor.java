package com.jtt.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static com.jtt.app.controller.UserController.UID_TOKEN_PREFIX;

/**
 * @author : zengshuaizhi
 * @date : 2019-06-30 13:29
 */
@Slf4j
@Component
public class WebSocketHandShakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 握手处理 鉴权过程
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws HandshakeFailureException
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("A new websocket handshake is running , processing ...");

        ServletServerHttpRequest httpRequest = (ServletServerHttpRequest) request;
        Long uid = Long.valueOf(httpRequest.getServletRequest().getParameter("uid"));
        String token = httpRequest.getServletRequest().getParameter("token");
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty(uid)) {
            String tokenInRedis = (String) redisTemplate.opsForHash().get(UID_TOKEN_PREFIX, uid);
            if (token.equals(tokenInRedis)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

        log.info("A new websocket handshake is successfully ! ");
    }
}
