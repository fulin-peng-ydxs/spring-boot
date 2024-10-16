package web.mvc.filter;

import commons.model.web.request.wrapper.CustomRequestWrapper;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 请求参数设置过滤器
 * 2024/10/16 下午5:47 
 * @author fulin-peng
 */

@Component
@WebFilter(filterName = "parameterChangingFilter",urlPatterns = {"/xxxx/xxx/*"})
public class RequestParameterChangingFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }
    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 包装原始请求，创建自定义请求对象
        CustomRequestWrapper wrappedRequest = new CustomRequestWrapper(httpRequest);
        // 将修改后的请求传递给下一个过滤器或 Controller
        likeQueryExecute(wrappedRequest);
        //other...
        chain.doFilter(wrappedRequest, response);
    }

    /**
     * 统一模糊查询处理
     * 2024/10/12 下午6:08
     * @author fulin-peng
     */
    public void likeQueryExecute(CustomRequestWrapper wrappedRequest){
        wrappedRequest.getParameterMap().forEach((key,value)-> wrappedRequest.setParameter(key,"%"+value[0]+"%"));
    }
}
