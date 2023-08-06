package web.mvc.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    /**
     * 统一处理自定义json序列化配置
     * @author fulin peng
     * 2023/8/4 0004 15:06
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.stream().filter(converter -> converter instanceof MappingJackson2HttpMessageConverter).forEach(converter -> {
            MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
            SimpleModule simpleModule = new SimpleModule();
            // 防止Long丢失精度
            // long -> String
            simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
            // Long -> String
            simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
            jsonConverter.getObjectMapper().registerModule(simpleModule);
        });
    }
}