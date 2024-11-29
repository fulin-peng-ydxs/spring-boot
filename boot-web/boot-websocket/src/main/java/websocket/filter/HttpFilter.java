package websocket.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author fulin-peng
 * 2024-11-22  10:22
 */
@WebFilter("/*")
public class HttpFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("HttpFilter");
    }

    @Override
    public void destroy() {

    }
}
