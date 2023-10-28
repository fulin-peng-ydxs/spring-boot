package websocket.basic.handler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
/**
 * websocket处理器，用来处理各种事件，如连接、消息接受等
 * 2023/10/27 00:02
 * @author pengshuaifeng
 */
public class MyWebSocketHandler extends TextWebSocketHandler {

    /**
     * 处理文本消息
     * 2023/10/27 00:14
     * @author pengshuaifeng
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 处理收到的WebSocket消息
        String payload = message.getPayload();
        // 这里可以对消息进行处理，并发送响应消息
        session.sendMessage(new TextMessage("Received: " + payload));
    }
}
