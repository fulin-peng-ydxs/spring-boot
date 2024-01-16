package web.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * swagger文档配置实例
     * 2024/1/14 18:56
     * @author pengshuaifeng
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()) //配置swagger文档信息
                .select()  //配置api的公开范围
                .apis(RequestHandlerSelectors.basePackage("web.mvc.controller"))  //设置需要公开的api接口包范围
                .build();
    }

    /**
     * 接口文档信息对象
     * 2024/1/14 18:51
     * @author pengshuaifeng
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("系统管理服务-API接口文档")  //文档标题
                .description("提供系统管理服务可用的接口列表及接口功能详情") //文档描述
                .termsOfServiceUrl("http://www.cnblogs.com/irving") //文档提供团队地址
                .version("1.0") //文档版本
                .build();
    }

}