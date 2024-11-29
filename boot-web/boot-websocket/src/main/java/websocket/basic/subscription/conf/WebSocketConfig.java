package websocket.basic.subscription.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import websocket.basic.subscription.handler.ConnectionHandler;
import websocket.basic.subscription.handler.ConnectionHandshakeInterceptor;

/**
 * websocket配置类
 * 2023/10/27 00:05
 * @author pengshuaifeng
 */
@Configuration
public class WebSocketConfig implements WebSocketConfigurer{

    /**
     * 注册websocket路由，http请求路径按照路由规则匹配转发到处理器进行事件处理
     * 2023/10/27 00:06
     * @author pengshuaifeng
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //注册处理器&路由映射
        registry.addHandler(connectionHandler(), "/ws/*")
                .addInterceptors(connectionHandshakeInterceptor())
                .setAllowedOrigins("*"); //允许跨域
    }

    /**
     * 配置处理器
     * 2023/10/27 00:05
     * @author pengshuaifeng
     */
    @Bean
    public ConnectionHandler connectionHandler() {
        return new ConnectionHandler();
    }

    public ConnectionHandshakeInterceptor connectionHandshakeInterceptor() {
        return new ConnectionHandshakeInterceptor();
    }

}
