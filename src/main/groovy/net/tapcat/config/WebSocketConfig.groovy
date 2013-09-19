package net.tapcat.config

import org.springframework.messaging.simp.config.MessageBrokerConfigurer
import org.springframework.messaging.simp.config.StompEndpointRegistry
import org.springframework.messaging.simp.config.WebSocketMessageBrokerConfigurer


//@Configuration
//@EnableWebSocketMessageBroker
//@EnableScheduling
//@ComponentScan(basePackages="org.springframework.samples")
class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socks").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerConfigurer configurer) {
        configurer.enableSimpleBroker("/queue/", "/topic/");
//		configurer.enableStompBrokerRelay("/queue/", "/topic/");
        configurer.setAnnotationMethodDestinationPrefixes("/app");
    }
}
