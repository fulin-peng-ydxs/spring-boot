package websocket.basic.subscription.handler;

import commons.web.ServerHttpRequestUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class ConnectionHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, 
                                    WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 获取请求头中的 token
        String token = ServerHttpRequestUtils.getHeader(request,"token");
        if(token==null){  //获取cookie中的token
            token = ServerHttpRequestUtils.getCookieValue(request, "token");
            if (token==null){ //获取url中的token
                token=ServerHttpRequestUtils.getQueryParamValue(request, "token");
            }
        }
        if (token != null)
            // 将需要的属性保存到 WebSocket Session 中
            attributes.put("token", token);
        attributes.put("request", request);
        return true; // 返回 true 允许握手
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                WebSocketHandler wsHandler, Exception exception) {
        // 握手完成后的操作
    }
}
