package commons.web;

import org.springframework.http.server.ServerHttpRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * ServerHttpRequest 工具类
 *
 * @author fulin-peng
 * 2024-12-06  17:21
 */
public class ServerHttpRequestUtils {

    /**
     * 获取请求头
     * 2024/12/6 下午5:24
     * @author fulin-peng
     */
    public static String getHeader(ServerHttpRequest request, String headerName) {
        return request.getHeaders().getFirst(headerName);
    }

    /**
     * 获取cookie
     * 2024/12/6 下午5:48
     * @author fulin-peng
     */
    public static Map<String,String> getCookie(ServerHttpRequest request){
        String cookies = request.getHeaders().getFirst("Cookie");
        if (cookies!=null){
            Map<String, String> cookieValues = new HashMap<>();
            for (String cookie : cookies.split(";")) {
                String[] cookieKeyAndValue = cookie.split("=");
                cookieValues.put(cookieKeyAndValue[0], cookieKeyAndValue[1]);
            }
            return cookieValues;
        }
        return null;
    }

    public static String getCookieValue(ServerHttpRequest request,String cookieName){
        Map<String, String> cookie = getCookie(request);
        if (cookie!=null)
            return cookie.get(cookieName);
        return null;
    }

    /**
     * 获取url参数
     * 2024/12/10 下午1:56
     * @author fulin-peng
     */
    public static Map<String,String> getQueryParams(ServerHttpRequest request){
        String query = request.getURI().getQuery();
        if (query!=null) {
            Map<String, String> queryValues = new HashMap<>();
            for (String param : query.split("&")){
                String[] paramKeyAndValue = param.split("=");
                queryValues.put(paramKeyAndValue[0], paramKeyAndValue[1]);
            }
            return queryValues;
        }
        return null;
    }

    public static String getQueryParamValue(ServerHttpRequest request,String paramName){
        Map<String, String> queryParams = getQueryParams(request);
        if (queryParams!=null)
            return queryParams.get(paramName);
        return null;
    }
}
