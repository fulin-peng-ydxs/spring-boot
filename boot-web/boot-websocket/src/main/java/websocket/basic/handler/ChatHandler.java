package websocket.basic.handler;


import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 聊天处理器
 * author: pengshuaifeng
 * 2023/10/27
 */
public class ChatHandler  extends TextWebSocketHandler {

    //连接的浏览器会话集合
    private Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    /**
     * 成功连接时
     * 2023/10/27 01:35
     * @author pengshuaifeng
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    /**
     * 处理文本消息：将消息同步给所有连接的客户端
     * 2023/10/28 19:17
     * @author pengshuaifeng
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        for (WebSocketSession client : sessions) {
            if (client != session) {
                client.sendMessage(message);
            }
        }
    }

    /**
     * 关闭连接时
     * 2023/10/27 01:40
     * @author pengshuaifeng
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
}
