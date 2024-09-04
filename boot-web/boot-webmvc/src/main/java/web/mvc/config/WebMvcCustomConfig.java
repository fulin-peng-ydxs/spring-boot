package web.mvc.config;

import commons.resolver.web.ExcelHandlerMethodArgumentResolver;
import commons.utils.ClassUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import java.util.LinkedList;
import java.util.List;

/**
 * web-mvc自定义配置类
 * @author fulin-peng
 * 2024-09-04  17:37
 */
@Configuration
public class WebMvcCustomConfig {

    public WebMvcCustomConfig(RequestMappingHandlerAdapter requestMappingHandlerAdapter){
        addCustomArgumentResolver(requestMappingHandlerAdapter);
    }

    private void addCustomArgumentResolver(RequestMappingHandlerAdapter handlerAdapter){
        //添加自定义Handler方法参数解析器
        List<HandlerMethodArgumentResolver> argumentResolvers = handlerAdapter.getArgumentResolvers();
        LinkedList<HandlerMethodArgumentResolver> newArgumentResolvers= new LinkedList<>();
        newArgumentResolvers.add(new ExcelHandlerMethodArgumentResolver());
        newArgumentResolvers.addAll(argumentResolvers);
        ClassUtils.setFieldValueWithMultistage("argumentResolvers.argumentResolvers",newArgumentResolvers,handlerAdapter);
    }
}
