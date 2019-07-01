package com.jtt.websocket;

import com.jtt.app.model.ImPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * @author : zengshuaizhi
 * @date : 2019-06-30 18:06
 */
@Slf4j
@Component
public class ImWebSocketHandShakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {

        log.info("Processing create new websocket principal!");

        ServletServerHttpRequest httpRequest = (ServletServerHttpRequest) request;
        String uid = httpRequest.getServletRequest().getParameter("uid");
        if (uid == null) {
            return null;
        }
        log.info("Processing create new websocket principal finished id = {}!", uid);
        return new ImPrincipal(uid);
    }
}
