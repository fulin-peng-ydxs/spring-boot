package websocket.basic.subscription.handler;

import commons.model.web.response.Response;
import commons.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 连接处理器
 *
 * @author fulin-peng
 * 2024-11-22  11:16
 */
@Slf4j
public class ConnectionHandler extends TextWebSocketHandler {

    //连接会话集合
    public final static Set<WebSocketSession> eventSessions =new HashSet<>(16);

    /**
     * 成功连接时
     * 2023/10/27 01:35
     * @author pengshuaifeng
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            Map<String, Object> attributes = session.getAttributes();
            String path = ((ServerHttpRequest) attributes.get("request")).getURI().getPath();
            log.info("ws连接path：{}", path);
            String[] paths = path.split("/");
            String target = paths[paths.length-1];
            log.info("ws连接target：{}",target);
            //认证处理
            Object token = attributes.get("token");
            Object principal=null;
//            Object principal = this.tokenService.toPrincipal(token==null?null:token.toString());
            if(principal==null)
                throw new RuntimeException("认证失败");
            if(target.equals("event")){
                eventSessions.add(session);
                attributes.put("name","event");
            }
            session.sendMessage(new TextMessage(JsonUtils.getString(Response.success())));
            log.info("ws连接成功");
        } catch (Exception e) {
            log.error("ws连接异常：",e);
            session.sendMessage(new TextMessage(JsonUtils.getString(Response.failure(e.getMessage()))));
        }
    }

    /**
     * 关闭连接时
     * 2023/10/27 01:40
     * @author pengshuaifeng
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Object name = session.getAttributes().get("name");
        if (name != null && name.equals("event"))
            eventSessions.remove(session);
    }
}