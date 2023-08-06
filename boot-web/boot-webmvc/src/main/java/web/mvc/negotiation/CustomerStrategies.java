package web.mvc.negotiation;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author PengFuLin
 * @version 1.0
 * @description: 自定义内容协商协议配置类
 * @date 2022/3/22 23:48
 */
@Component
public class CustomerStrategies extends WebMvcConfigurerAdapter {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        //第一中：添加自定义的参数协议下的请求类型
        Map<String, MediaType> mediaTypes = new HashMap<>();
        mediaTypes.put("demo",MediaType.parseMediaType("application.yml/x-demo"));
        mediaTypes.forEach(configurer::mediaType);
        //第二种：添加自定义的内容协议:需要注意的是，
        //如果手动地添加了协议，则默认的协议将不会自动的加入：默认且不支持自定义加入多个，只能实现一个
        configurer.defaultContentTypeStrategy(new CustomerContentNegotiationStrategy());
    }
}
