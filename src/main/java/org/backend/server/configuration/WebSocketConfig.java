package org.backend.server.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket configuration for real-time communication in the ticket system.
 * Configures message broker endpoints and STOMP protocol settings with SockJS support.
 * Enables cross-origin requests and sets appropriate buffer limits for streaming.
 * @Configuration marks this as a Spring configuration class.
 * @EnableWebSocketMessageBroker enables WebSocket message handling.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configures the message broker with a simple in-memory broker for /summery endpoint.
     * @param config MessageBrokerRegistry for broker configuration
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/summery");
    }

    /**
     * Sets up STOMP endpoints with SockJS fallback support and streaming configurations.
     * @param registry StompEndpointRegistry for endpoint registration
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS()
                .setStreamBytesLimit(1024 * 1024)
                .setHttpMessageCacheSize(1024 * 1024);
    }
}