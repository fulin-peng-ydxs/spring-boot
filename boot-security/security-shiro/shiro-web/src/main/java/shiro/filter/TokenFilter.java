package shiro.filter;

import commons.holder.ServletHolder;
import commons.model.web.response.Response;
import commons.utils.StringUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import shiro.entity.shiro.AuthToken;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * token认证过滤器
 * 2024/8/4 21:26
 * @author pengshuaifeng
 */
@Slf4j
@Setter
public class TokenFilter extends BasicHttpAuthenticationFilter {

    /**
     * 是否允许访问：用户认证
     * 2024/8/4 20:52
     * @author pengshuaifeng
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            //先判断会话是否允许访问，再根据token进行认证
            return super.isAccessAllowed(request, response, mappedValue) || isAccessAllowedExecute(request, response);
        } catch (Exception e) {
            log.error("用户认证异常",e);
            onAccessDeniedExecute(e);
        }
        return false;
    }



    /**
     * 认证处理
     * 2024/8/4 21:31
     * @author pengshuaifeng
     */
    private boolean isAccessAllowedExecute(ServletRequest request, ServletResponse response){
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //从请求中获取token
        String tokenName="token";
        String token = ServletHolder.getRequestParam(tokenName, httpServletRequest);
        token=StringUtils.isEmpty(token)?ServletHolder.getRequestHeader(tokenName):token;
        if(StringUtils.isEmpty(token)){
            return false;
        }
        //token校验
        getSubject(request, response).login(new AuthToken(token,token));
        return true;
    }

    /**
     * 认证失败处理
     * @author pengshuaifeng
     * 2024/8/4 21:31
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response){
        onAccessDeniedExecute(null);
        return false;
    }

    /**
     * 认证失败处理
     * @author pengshuaifeng
     * 2024/8/4 21:31
     */
    private void onAccessDeniedExecute(Exception exception){
        HttpServletResponse response = ServletHolder.getResponse();
        if(response.getStatus()!=HttpStatus.OK.value())
            return;
        boolean haveException = exception != null;
        //TODO 如果存在业务异常，可根据异常情况处理
        String message = haveException?"用户认证异常": "用户未认证";
        if(ServletHolder.isJsonRequest())  //json响应
            ServletHolder.responseToJson(Response.failure(message), haveException?HttpStatus.INTERNAL_SERVER_ERROR:HttpStatus.UNAUTHORIZED);
        else{ //重定向
            String redirectUrl=haveException?"/500.html":"/login_interface.html";
            ServletHolder.redirect(redirectUrl);
        }
    }
}
