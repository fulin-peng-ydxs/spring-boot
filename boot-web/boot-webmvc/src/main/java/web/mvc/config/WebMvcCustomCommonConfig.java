package web.mvc.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import commons.validator.ValidatorService;

/**
 * web-mvc自定义组件配置
 * @author pengshuaifeng
 * 2023/12/24
 */
@Import(ValidatorService.class)
@Configuration
public class WebMvcCustomCommonConfig {

}
