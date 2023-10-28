package websocket.basic.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import websocket.basic.handler.ChatHandler;
import websocket.basic.handler.MyWebSocketHandler;

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
        //注册处理器与路由映射关系
        registry.addHandler(myHandler(), "/websocket")
                .setAllowedOrigins("*"); //允许跨域
        //注册群聊处理器路由
        registry.addHandler(chatHandler(), "/websocket/chat").setAllowedOrigins("*");
    }

    /**
     * 配置处理器
     * 2023/10/27 00:05
     * @author pengshuaifeng
     */
    @Bean
    public MyWebSocketHandler myHandler() {
        return new MyWebSocketHandler();
    }

    /**
     * 聊天处理器
     * 2023/10/27 01:30
     * @author pengshuaifeng
     */
    @Bean
    public WebSocketHandler chatHandler(){
        return new ChatHandler();
    }
}
