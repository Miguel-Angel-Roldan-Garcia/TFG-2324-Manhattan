package us.es.migrolgar2.manhattan.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
//		config.enableSimpleBroker("/topic/");
//		config.setApplicationDestinationPrefixes("/app");
		config.enableSimpleBroker("/lobby/", "/game/");
		config.setApplicationDestinationPrefixes("/lobby-msgs", "/game-msgs");
		
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/lobby-ws")
				.setAllowedOrigins("http://localhost", "https://localhost", "ws://localhost")
				.withSockJS();
		registry.addEndpoint("/game-ws")
				.setAllowedOrigins("http://localhost", "https://localhost", "ws://localhost")
				.withSockJS();
	}

}
