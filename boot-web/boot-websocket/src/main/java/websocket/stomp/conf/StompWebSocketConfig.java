package websocket.stomp.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		//portfolio是 WebSocket客户端需要连接WebSocket以进行握手的端点的HTTP URL。
		registry.addEndpoint("/portfolio").setAllowedOrigins("*");
		//portfolio是 SockJS客户端需要连接WebSocket以进行握手的端点的HTTP URL。
		registry.addEndpoint("/stomp/socket.js/portfolio").setAllowedOrigins("*").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		//目标标头以/app开头的STOMP消息将路由到@Controller类中的@MessageMapping方法:设置请求处理路径前缀
		config.setApplicationDestinationPrefixes("/stomp");
		//使用内置消息代理进行订阅和广播，将其目标标头以/topic或/queue开头的消息路由到代理。
		config.enableSimpleBroker("/topic", "/queue");
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registry) {

	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {

	}

	@Override
	public void configureClientOutboundChannel(ChannelRegistration registration) {

	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {

	}

	@Override
	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
		return false;
	}

}