package websocket.socketjs.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import websocket.socketjs.handler.SocketJSHandler;

@Configuration
public class SocketJsWebSocketConfig implements WebSocketConfigurer {

	/**注册websocket路由
	 * 2023/10/27 00:50
	 * @author pengshuaifeng
	 */
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		/*
		 * withSockJS()：表示在WebSocket连接上启用SockJS支持。SockJS是一个JavaScript库，它提供了一种跨浏览器的方式来处理WebSocket连接。
		 * SockJS可以在不支持原生WebSocket的浏览器上模拟WebSocket，以确保在各种浏览器中实现WebSocket通信的兼容性。
		 */
		registry.addHandler(socketJsHandler(), "/socket.js").setAllowedOrigins("*")
				.withSockJS();
	}

	@Bean
	public WebSocketHandler socketJsHandler() {
		return new SocketJSHandler();
	}

}