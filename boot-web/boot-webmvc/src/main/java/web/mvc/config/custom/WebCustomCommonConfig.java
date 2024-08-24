package web.mvc.config.custom;


import commons.validator.ValidatorService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * web自定义组件配置
 * @author pengshuaifeng
 * 2023/12/24
 */
@Import(ValidatorService.class)
@Configuration
@EnableConfigurationProperties(WebCustomCommonProperties.class)
public class WebCustomCommonConfig {

}
