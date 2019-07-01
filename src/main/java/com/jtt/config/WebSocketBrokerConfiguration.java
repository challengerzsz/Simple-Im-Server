package com.jtt.config;

import com.jtt.websocket.ImWebSocketHandShakeHandler;
import com.jtt.websocket.WebSocketHandShakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author : zengshuaizhi
 * @date : 2019-06-29 22:39
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfiguration implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private WebSocketHandShakeInterceptor webSocketHandShakeInterceptor;

    @Autowired
    private ImWebSocketHandShakeHandler imWebSocketHandShakeHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/im-server")
                .setHandshakeHandler(imWebSocketHandShakeHandler)
                .setAllowedOrigins("*").withSockJS()
                .setInterceptors(webSocketHandShakeInterceptor);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic/private", "/topic/public", "/topic/test");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

}
