package websocket.basic.subscription.handler;

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
        String token = request.getHeaders().getFirst("token");
        if(token==null){  //获取cookie中的token
            String cookies = request.getHeaders().getFirst("Cookie");
            if (cookies!=null){
                for (String cookie : cookies.split(";")) {
                    String[] cookieKeyAndValue = cookie.split("=");
                    if (cookieKeyAndValue[0].equals("token")) {
                        token = cookieKeyAndValue[1];
                    }
                }
            }
            if (token==null){ //获取url中的token
                String query = request.getURI().getQuery();
                if (query!=null) {
                    for (String param : query.split("&")){
                        String[] paramKeyAndValue = param.split("=");
                        if (paramKeyAndValue[0].equals("token"))
                            token = paramKeyAndValue[1];
                    }
                }
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
